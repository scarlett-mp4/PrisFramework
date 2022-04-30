package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Arrays;
import java.util.List;

public class PartyPromote extends SubCommand {
    @Override
    public List<String> getNames() {
        return Arrays.asList("promote", "p", "mod");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisApi api = new PrisApi();
        OfflinePrisPlayer target = null;

        try {
            target = api.wrapOfflinePlayer(args[1]);

            if (!p.toOfflinePlayer().inParty()) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Promote.NotInParty"));
                return;
            }

            if (!target.inParty()) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Promote.PlayerNotInParty"));
                return;
            }

            if (!p.toOfflinePlayer().getParty().containsPlayer(target)) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Promote.PlayerNotInParty"));
                return;
            }

            if (!p.toOfflinePlayer().getParty().getRole(p.toOfflinePlayer()).equals("leader")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Promote.NoPermission"));
                return;
            }

            if (p.toOfflinePlayer().getParty().getRole(target).equals("leader")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Promote.AlreadyLeader"));
                return;
            }
        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.NotOnline"));
        }

        if (target != null) {
            OfflinePrisPlayer leader = p.toOfflinePlayer();
            PrisParty party = leader.getParty();

            if (args.length > 2) {
                switch (args[2].toLowerCase()) {
                    case "mod":
                        mod(leader, target);
                        return;
                    case "leader":
                        leader(leader, target);
                        return;
                    default:
                        if (party.getRole(target).equals("member")) {
                            mod(leader, target);
                            return;
                        }
                        if (party.getRole(target).equals("mod")) {
                            leader(leader, target);
                            return;
                        }
                }
            } else {
                if (party.getRole(target).equals("member")) {
                    mod(leader, target);
                    return;
                }
                if (party.getRole(target).equals("mod")) {
                    leader(leader, target);
                    return;
                }
            }


            target.getParty().setMod(target);

            String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.Promote.Promoted")).replaceAll(target.getUsername());
            for (OfflinePrisPlayer offlinePrisPlayer : p.toOfflinePlayer().getParty().getUsers().keySet()) {
                try {
                    PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                    prisPlayer.sendMessage(message);
                } catch (Exception ignored) {
                }
            }


        }
    }

    private void mod(OfflinePrisPlayer leader, OfflinePrisPlayer target) {
        PrisApi api = new PrisApi();
        leader.getParty().setMod(target);

        for (OfflinePrisPlayer offlinePrisPlayer : leader.getParty().getUsers().keySet()) {
            try {
                PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(prisPlayer.getLanguageFile(), "Party.Promote.ModPromoted")).replaceAll(target.getUsername());
                prisPlayer.sendMessage(message);
            } catch (Exception ignored) {
            }
        }
    }

    private void leader(OfflinePrisPlayer leader, OfflinePrisPlayer target) {
        PrisApi api = new PrisApi();
        leader.getParty().setLeader(target);
        leader.getParty().setMod(leader);

        for (OfflinePrisPlayer offlinePrisPlayer : leader.getParty().getUsers().keySet()) {
            try {
                PrisPlayer prisPlayer = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(prisPlayer.getLanguageFile(), "Party.Promote.LeaderPromoted")).replaceAll(target.getUsername());
                prisPlayer.sendMessage(message);
            } catch (Exception ignored) {
            }
        }
    }
}
