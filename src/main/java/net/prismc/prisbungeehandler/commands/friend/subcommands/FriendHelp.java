package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class FriendHelp extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("help");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        Configuration lang = p.getLanguageFile();

        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.General.Spacer")));
        UtilApi.sendCenteredMessage(p, UtilApi.getString(lang, "Friends.Help.Header"));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Accept")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Add")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Deny")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.List")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Notifications")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Toggle")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Remove")));
        p.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.General.Spacer")));
    }
}
