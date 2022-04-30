package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class PartyLeave extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("leave");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisBungeeHandler instance = PrisBungeeHandler.getInstance();
        PrisApi api = new PrisApi();

        try {
            if (p.toOfflinePlayer().getParty() == null) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Leave.NotInParty"));
                return;
            }
        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Leave.NotInParty"));
            return;
        }
        OfflinePrisPlayer leader = p.toOfflinePlayer().getParty().getLeader();

        if (p.toOfflinePlayer() == leader) {
            PartyDisband disband = new PartyDisband();
            disband.disbandParty(p);
            return;
        }

        p.toOfflinePlayer().getParty().removeUser(p.toOfflinePlayer());
        p.toOfflinePlayer().leaveParty();
        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Leave.YouLeft"));

        if (!leader.getParty().isOpen()) {
            if (leader.getParty().getUsers().size() <= 1) {
                try {
                    PrisPlayer player = api.wrapPlayer(ProxyServer.getInstance().getPlayer(leader.getUniqueId()));
                    player.sendMessage(UtilApi.getString(player.getLanguageFile(), "Party.Leave.NotEnoughPlayers"));
                    leader.getParty().disband();
                } catch (Exception ignored) {
                }
            }
        }

        ProxyServer.getInstance().getScheduler().runAsync(instance, () -> {
            try {
                for (OfflinePrisPlayer player : leader.getParty().getUsers().keySet()) {
                    try {
                        PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(player.getUniqueId()));
                        String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(prisPlayer.getLanguageFile(), "Party.Leave.LeftTheParty")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
                        prisPlayer.sendMessage(message);
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        });
    }
}
