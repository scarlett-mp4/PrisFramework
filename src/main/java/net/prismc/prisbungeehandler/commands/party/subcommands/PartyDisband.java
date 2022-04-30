package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Arrays;
import java.util.List;

public class PartyDisband extends SubCommand {

    @Override
    public List<String> getNames() {
        return Arrays.asList("disband", "delete", "del");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        disbandParty(p);
    }

    public void disbandParty(PrisPlayer p) {

        try {
            if (p.toOfflinePlayer().getParty() == null) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Disband.NotInParty"));
                return;
            }
        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Disband.NotInParty"));
            return;
        }

        if (p.toOfflinePlayer() != p.toOfflinePlayer().getParty().getLeader()) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Disband.MustBeLeader"));
            return;
        }

        p.toOfflinePlayer().getParty().disband();
    }
}
