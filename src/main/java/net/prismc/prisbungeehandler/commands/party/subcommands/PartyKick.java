package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class PartyKick extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("kick");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisApi api = new PrisApi();

        try {
            OfflinePrisPlayer target = api.wrapOfflinePlayer(args[1]);

            if (p.toOfflinePlayer() == target) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.CantKickSelf"));
                return;
            }

            if (p.toOfflinePlayer().getParty() == null) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.NotInParty"));
                return;
            }

            if (target.getParty() == null) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.PlayerNotInParty"));
                return;
            }

            if (!p.toOfflinePlayer().getParty().containsPlayer(target)) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.PlayerNotInParty"));
                return;
            }

            if (p.toOfflinePlayer().getParty().getRole(p.toOfflinePlayer()).equals("member")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.NoPermission"));
                return;
            }

            if (p.toOfflinePlayer().getParty().getRole(p.toOfflinePlayer()).equals("mod") && p.toOfflinePlayer().getParty().getRole(target).equals("mod")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.CantKickPlayer"));
                return;
            }

            if (p.toOfflinePlayer().getParty().getRole(p.toOfflinePlayer()).equals("mod") && p.toOfflinePlayer().getParty().getRole(target).equals("leader")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.CantKickPlayer"));
                return;
            }

            p.toOfflinePlayer().getParty().removeUser(target);
            target.leaveParty();
            try {
                PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(target.getUniqueId()));
                prisPlayer.sendMessage(UtilApi.getString(prisPlayer.getLanguageFile(), "Party.Kick.YouWereKicked"));
            } catch (Exception ignored) {
            }

            String message = PatternCollection.MOD_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.Kick.KickedPlayer")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
            message = PatternCollection.PLAYER_PATTERN.matcher(message).replaceAll(target.getRankColor() + target.getUsername());
            for (OfflinePrisPlayer offlinePrisPlayer : p.toOfflinePlayer().getParty().getUsers().keySet()) {
                try {
                    PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                    prisPlayer.sendMessage(message);
                } catch (Exception ignored) {
                }
            }

            if (p.toOfflinePlayer().getParty().getUsers().size() <= 1) {
                if (!p.toOfflinePlayer().getParty().isOpen()) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Kick.NotEnoughPlayers"));
                    p.toOfflinePlayer().getParty().disband();
                }
            }
        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.NotOnline"));
        }
    }
}
