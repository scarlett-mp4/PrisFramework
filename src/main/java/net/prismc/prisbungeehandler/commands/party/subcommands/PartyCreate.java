package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Arrays;
import java.util.List;

public class PartyCreate extends SubCommand {

    @Override
    public List<String> getNames() {
        return Arrays.asList("create", "c");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {

        if (p.toOfflinePlayer().inParty()) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Create.AlreadyInParty"));
            return;
        }

        try {
            if (args[1].equals("closed")) {
                create(p, false);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Create.Created"));
            } else if (args[1].equals("open")) {
                create(p, true);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Create.CreatedOpen"));
            } else {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Create.InvalidType"));
            }
        } catch (Exception e) {
            create(p, false);
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Create.Created"));
        }
    }

    private void create(PrisPlayer p, boolean b) {
        PrisParty newParty = new PrisParty(p.toOfflinePlayer(), b);
        p.toOfflinePlayer().addParty(newParty);
        newParty.setLeader(p.toOfflinePlayer());
    }
}
