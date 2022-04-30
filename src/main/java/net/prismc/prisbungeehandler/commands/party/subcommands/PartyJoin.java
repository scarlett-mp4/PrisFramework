package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class PartyJoin extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("join");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisApi api = new PrisApi();

        try {
            if (args.length > 1) {
                if (!p.toOfflinePlayer().inParty()) {
                    OfflinePrisPlayer offlinePrisPlayer = api.wrapOfflinePlayer(args[1]);
                    if (offlinePrisPlayer.inParty()) {
                        if (offlinePrisPlayer.getParty().isOpen()) {
                            joinParty(p, offlinePrisPlayer);
                            return;
                        } else {
                            if (p.containsIncomingPartyInvite(offlinePrisPlayer)) {
                                joinParty(p, offlinePrisPlayer);
                                return;
                            } else {
                                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Join.NoRequest"));
                            }
                        }
                    } else {
                        if (p.containsIncomingPartyInvite(offlinePrisPlayer)) {
                            PrisParty newParty = new PrisParty(offlinePrisPlayer, false);
                            offlinePrisPlayer.addParty(newParty);
                            newParty.setLeader(offlinePrisPlayer);
                            joinParty(p, offlinePrisPlayer);
                            return;
                        } else {
                            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Join.NoRequest"));
                        }
                    }
                } else {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Join.AlreadyInAParty"));
                }
            } else {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.MissingArgs"));
            }
        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Join.NoRequest"));
        }
    }

    public void joinParty(PrisPlayer toJoin, OfflinePrisPlayer leader) {
        PrisApi api = new PrisApi();

        try {
            if (!leader.isOnline()) {
                toJoin.sendMessage(PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(toJoin.getLanguageFile(), "Party.Join.LeaderMustBeOnline")).replaceAll(leader.getRankColor() + leader.getUsername()));
                return;
            }
            if (leader.getMaxPartyPlayers() <= leader.getParty().getUsers().size()) {
                toJoin.sendMessage(PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(toJoin.getLanguageFile(), "Party.Join.Full")).replaceAll(leader.getRankColor() + leader.getUsername()));
                return;
            }
        } catch (Exception e) {
            toJoin.sendMessage(PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(toJoin.getLanguageFile(), "Party.Join.LeaderMustBeOnline")).replaceAll(leader.getRankColor() + leader.getUsername()));
            return;
        }

        try {
            ProxyServer.getInstance().getScheduler().cancel(toJoin.getIncomingPartyInviteTask(leader));
        } catch (Exception ignored) {
        }
        toJoin.removeIncomingPartyInvite(leader);

        if (leader.inParty()) {
            if (leader.getParty().getLeader().getUniqueId() == leader.getUniqueId()) {
                leader.getParty().setMember(toJoin.toOfflinePlayer());
                toJoin.toOfflinePlayer().addParty(leader.getParty());
            }
        } else {
            PrisParty newParty = new PrisParty(leader, false);
            leader.addParty(newParty);
            toJoin.toOfflinePlayer().addParty(newParty);
            newParty.setLeader(leader);
            newParty.setMember(toJoin.toOfflinePlayer());
        }

        for (OfflinePrisPlayer player : leader.getParty().getUsers().keySet()) {
            try {
                PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(player.getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(prisPlayer.getLanguageFile(), "Party.Join.HasJoined")).replaceAll(toJoin.toOfflinePlayer().getRankColor() + toJoin.getUsername());
                prisPlayer.sendMessage(message);
            } catch (Exception ignored) {
            }
        }
    }
}
