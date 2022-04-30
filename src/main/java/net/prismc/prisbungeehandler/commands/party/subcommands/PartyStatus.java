package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class PartyStatus extends SubCommand {
    @Override
    public List<String> getNames() {
        return Collections.singletonList("status");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisApi api = new PrisApi();

        if (!p.toOfflinePlayer().inParty()) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.NotInParty"));
            return;
        }

        if (p.toOfflinePlayer().getParty().getRole(p.toOfflinePlayer()).equals("member")) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.NoPermission"));
            return;
        }

        PrisParty party = p.toOfflinePlayer().getParty();
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "open":
                    party.setOpen(true);
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.Open"));
                    return;
                case "closed":
                    party.setOpen(false);
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.Closed"));
                    return;
                default:
                    if (party.isOpen()) {
                        party.setOpen(false);
                        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.Closed"));
                    } else {
                        party.setOpen(true);
                        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.Open"));
                    }
            }
        } else {
            if (party.isOpen()) {
                party.setOpen(false);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.Closed"));
            } else {
                party.setOpen(true);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Status.Open"));
            }
        }
    }
}
