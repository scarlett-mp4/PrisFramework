package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.config.Configuration;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class PartyHelp extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("help");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        Configuration lang = p.getLanguageFile();

        p.sendMessage(UtilApi.getString(lang, "Party.General.Spacer"));
        UtilApi.sendCenteredMessage(p, UtilApi.getString(lang, "Party.Help.Header"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Create"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Invite"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Join"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Kick"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Leave"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Disband"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.List"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Status"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Promote"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Warp"));

        p.sendMessage(UtilApi.getString(lang, "Party.Help.Chat"));
        p.sendMessage(UtilApi.getString(lang, "Party.Help.Mute"));
        p.sendMessage(UtilApi.getString(lang, "Party.General.Spacer"));
    }
}
