package net.prismc.prisbungeehandler.parties;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.events.pris.PrisJoinEvent;
import net.prismc.prisbungeehandler.events.pris.PrisQuitEvent;
import net.prismc.prisbungeehandler.events.pris.PrisServerConnectRequestEvent;
import net.prismc.prisbungeehandler.events.pris.PrisServerSwitchEvent;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;

import java.util.concurrent.TimeUnit;

public class PartyListeners implements Listener {

    long delay = 5; // Delay for leader rejoin in minutes

    @EventHandler
    public void onServerSwitchAttempt(PrisServerConnectRequestEvent e) {
        PrisPlayer p = e.getPlayer();

        if (p.toOfflinePlayer().getParty() != null) {
            PrisParty party = p.toOfflinePlayer().getParty();

            if (p.isPartyOverride()) {
                return;
            }

            if (party.getRole(p.toOfflinePlayer()).equals("leader")) {
                for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                    if (!offlinePrisPlayer.isOnline()) {
                        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Events.OfflinePlayers"));
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            if (!party.getRole(p.toOfflinePlayer()).equals("leader")) {
                if (!e.getTarget().getName().contains("lobby") && !e.getTarget().getName().contains("hub")) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Events.CantSwitchServers"));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onServerSwitch(PrisServerSwitchEvent e) {
        PrisApi api = new PrisApi();
        PrisPlayer p = e.getPlayer();

        if (p.toOfflinePlayer().getParty() != null) {
            PrisParty party = p.toOfflinePlayer().getParty();

            if (party.getRole(p.toOfflinePlayer()).equals("leader")) {
                if (!p.getServer().getInfo().getName().contains("lobby") && !p.getServer().getInfo().getName().contains("hub")) {
                    for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                        if (offlinePrisPlayer != p.toOfflinePlayer()) {
                            try {
                                PrisPlayer player = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                                player.connect(p.getServer().getInfo(), true);
                            } catch (Exception ignore) {

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeaderQuit(PrisQuitEvent e) {
        PrisApi api = new PrisApi();
        PrisPlayer p = e.getPlayer();

        if (p.getIncomingPartyInvites().size() != 0) {
            for (int i : p.getIncomingPartyInvites().keySet()) {
                PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(api.wrapOfflinePlayer(i).getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(prisPlayer.getLanguageFile(), "Party.Invite.TimedOutTo")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
                prisPlayer.sendMessage(message);
                ProxyServer.getInstance().getScheduler().cancel(p.getIncomingPartyInviteTask(prisPlayer.toOfflinePlayer()));
            }
        }

        if (p.getIncomingFriendRequests().size() != 0) {
            for (int i : p.getIncomingFriendRequests().keySet()) {
                PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(api.wrapOfflinePlayer(i).getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(prisPlayer.getLanguageFile(), "Friends.Add.TimedOutTo")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
                prisPlayer.sendMessage(message);
                ProxyServer.getInstance().getScheduler().cancel(p.getIncomingFriendRequestTask(prisPlayer.toOfflinePlayer()));
            }
        }

        if (p.toOfflinePlayer().getParty() != null) {
            PrisParty party = p.toOfflinePlayer().getParty();
            if (p.toOfflinePlayer() == party.getLeader()) {

                if (p.toOfflinePlayer().getParty().getUsers().size() <= 1) {
                    p.toOfflinePlayer().getParty().disband();
                    return;
                }

                for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                    try {
                        PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                        prisPlayer.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.Spacer"));
                        UtilApi.sendCenteredMessage(prisPlayer, UtilApi.getString(p.getLanguageFile(), "Party.Events.LeaderDisconnected"));
                        UtilApi.sendCenteredMessage(prisPlayer, UtilApi.getString(p.getLanguageFile(), "Party.Events.TimeToReconnect"));
                        prisPlayer.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.Spacer"));
                    } catch (Exception ignore) {
                    }
                }

                int id = ProxyServer.getInstance().getScheduler().schedule(PrisBungeeHandler.getInstance(), party::disband, delay, TimeUnit.MINUTES).getId();
                party.setLeaderQuitID(id);
            }
        }
    }

    @EventHandler
    public void onLeaderJoin(PrisJoinEvent e) {
        PrisApi api = new PrisApi();
        PrisPlayer p = e.getPlayer();

        if (p.toOfflinePlayer().getParty() != null) {
            PrisParty party = p.toOfflinePlayer().getParty();
            if (p.toOfflinePlayer() == party.getLeader()) {

                for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                    try {
                        PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                        prisPlayer.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.Spacer"));
                        UtilApi.sendCenteredMessage(prisPlayer, UtilApi.getString(p.getLanguageFile(), "Party.Events.LeaderReconnected"));
                        UtilApi.sendCenteredMessage(prisPlayer, UtilApi.getString(p.getLanguageFile(), "Party.Events.GetBackInThere"));
                        prisPlayer.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.Spacer"));
                    } catch (Exception ignore) {
                    }
                }

                ProxyServer.getInstance().getScheduler().cancel(party.getLeaderQuitID());
            }
        }
    }

    @EventHandler
    public void onUserQuit(PrisQuitEvent e) {
        try {
            PartyCommunication.update(e.getPlayer().toOfflinePlayer().getParty());
        } catch (Exception ignore) {
        }
    }

    @EventHandler
    public void onUserJoin(PrisJoinEvent e) {
        try {
            PartyCommunication.update(e.getPlayer().toOfflinePlayer().getParty());
        } catch (Exception ignore) {
        }
    }
}