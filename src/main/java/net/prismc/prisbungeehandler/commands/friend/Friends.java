package net.prismc.prisbungeehandler.commands.friend;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.commands.friend.subcommands.*;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.ArrayList;

public class Friends extends Command {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public Friends(String name, String permission, String... alias) {
        super(name, permission, alias);
        subcommands.add(new FriendAdd());
        subcommands.add(new FriendRemove());
        subcommands.add(new FriendList());
        subcommands.add(new FriendDeny());
        subcommands.add(new FriendAccept());
        subcommands.add(new FriendToggle());
        subcommands.add(new FriendNotifications());
        subcommands.add(new FriendHelp());
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            PrisApi api = new PrisApi();
            PrisPlayer player = api.wrapPlayer((ProxiedPlayer) commandSender);
            Configuration lang = player.getLanguageFile();

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    for (String s : getSubcommands().get(i).getNames()) {
                        if (args[0].equalsIgnoreCase(s)) {
                            getSubcommands().get(i).perform(player, args);
                            return;
                        }
                    }
                }
                try {
                    PrisPlayer receiver = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(args[0]));
                    FriendAdd add = new FriendAdd();
                    add.altAddFriend(player, receiver);
                    return;
                } catch (Exception ignored) {
                }
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.General.UnknownCommand")));
            } else {
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.General.Spacer")));
                UtilApi.sendCenteredMessage(player, UtilApi.getString(lang, "Friends.Help.Header"));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Accept")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Add")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Deny")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.List")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Notifications")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Toggle")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.Help.Remove")));
                player.sendMessage(new TextComponent(UtilApi.getString(lang, "Friends.General.Spacer")));
            }
        }
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

}