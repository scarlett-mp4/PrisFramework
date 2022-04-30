package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Arrays;
import java.util.List;

public class PartyWarp extends SubCommand {

    @Override
    public List<String> getNames() {
        return Arrays.asList("warp", "tp", "teleport", "w");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        try {
            PrisApi api = new PrisApi();
            PrisParty party = p.toOfflinePlayer().getParty();

            if (!party.getRole(p.toOfflinePlayer()).equals("leader")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Warp.NoPermission"));
                return;
            }

            for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                if (offlinePrisPlayer != p.toOfflinePlayer()) {
                    try {
                        PrisPlayer player = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                        player.connect(p.getServer().getInfo(), true);
                        player.sendMessage(UtilApi.getString(player.getLanguageFile(), "Party.Warp.YouWarped"));
                    } catch (Exception ignore) {
                    }
                }
            }

            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Warp.PlayersWarped"));
        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Warp.NotInParty"));
        }
    }
}
