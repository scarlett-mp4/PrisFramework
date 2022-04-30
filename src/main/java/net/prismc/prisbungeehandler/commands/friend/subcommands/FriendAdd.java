package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FriendAdd extends SubCommand {

    long time = 5; // Timeout for friend request in minutes

    @Override
    public List<String> getNames() {
        return Collections.singletonList("add");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisBungeeHandler instance = PrisBungeeHandler.getInstance();
        PrisApi playerApi = new PrisApi();

        if (args.length > 1) {
            try {
                PrisPlayer receiver = playerApi.wrapPlayer(instance.getProxy().getPlayer(args[1]));

                if (p.containsFriendCache(receiver.toOfflinePlayer())) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.AlreadyFriends"));
                    return;
                }

                if (receiver == p) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.CantFriendYourself"));
                    return;
                }

                if (receiver.containsIncomingFriendRequest(p.toOfflinePlayer())) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.AlreadySent"));
                    return;
                }

                if (p.containsIncomingFriendRequest(receiver.toOfflinePlayer())) {
                    FriendAccept accept = new FriendAccept();
                    accept.acceptFriendRequest(p, receiver.toOfflinePlayer());
                    return;
                }

                if (receiver.toOfflinePlayer().getSetting(0).equals("0")) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.Toggled"));
                    return;
                }

                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.Request")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
                TextComponent ADD = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.ADD"));
                TextComponent OR = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.OR"));
                TextComponent DENY = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.DENY"));
                ADD.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.ADDHover"))));
                OR.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("")));
                DENY.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.DENYHover"))));
                ADD.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + p.getUsername()));
                OR.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ""));
                DENY.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + p.getUsername()));
                ComponentBuilder builder = new ComponentBuilder().append(ADD).append(OR).append(DENY);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.Sent"));

                receiver.sendMessage(UtilApi.getString(receiver.getLanguageFile(), "Friends.General.Spacer"));
                UtilApi.sendCenteredMessage(receiver, message);
                UtilApi.sendCenteredMessage(receiver, builder);
                receiver.sendMessage(UtilApi.getString(receiver.getLanguageFile(), "Friends.General.Spacer"));

                int id = timeOut(p, receiver);
                receiver.addIncomingFriendRequest(p.toOfflinePlayer().getID(), id);

            } catch (Exception e) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.General.NotOnline"));
            }
        } else {
            p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.General.MissingArgs")));
        }
    }

    public void altAddFriend(PrisPlayer p, PrisPlayer receiver) {
        try {
            if (p.containsFriendCache(receiver.toOfflinePlayer())) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.AlreadyFriends"));
                return;
            }
        } catch (Exception ignored) {
        }

        if (receiver == p) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.CantFriendYourself"));
            return;
        }

        if (receiver.containsIncomingFriendRequest(p.toOfflinePlayer())) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.AlreadySent"));
            return;
        }

        if (p.containsIncomingFriendRequest(receiver.toOfflinePlayer())) {
            FriendAccept accept = new FriendAccept();
            accept.acceptFriendRequest(p, receiver.toOfflinePlayer());
            return;
        }

        if (receiver.toOfflinePlayer().getSetting(0).equals("0")) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.Toggled"));
            return;
        }

        String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.Request")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
        receiver.sendMessage(UtilApi.getString(receiver.getLanguageFile(), "Friends.General.Spacer"));
        UtilApi.sendCenteredMessage(receiver, message);
        TextComponent ADD = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.ADD"));
        TextComponent OR = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.OR"));
        TextComponent DENY = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.DENY"));
        ADD.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.ADDHover"))));
        OR.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("")));
        DENY.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.DENYHover"))));
        ADD.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + p.getUsername()));
        OR.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ""));
        DENY.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + p.getUsername()));
        ComponentBuilder builder = new ComponentBuilder().append(ADD).append(OR).append(DENY);
        UtilApi.sendCenteredMessage(receiver, builder);
        receiver.sendMessage(UtilApi.getString(receiver.getLanguageFile(), "Friends.General.Spacer"));
        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Add.Sent"));
        int id = timeOut(p, receiver);
        receiver.addIncomingFriendRequest(p.toOfflinePlayer().getID(), id);
    }

    public int timeOut(PrisPlayer sender, PrisPlayer receiver) {
        return ProxyServer.getInstance().getScheduler().schedule(PrisBungeeHandler.getInstance(), () -> {
            sender.removeIncomingFriendRequest(receiver.toOfflinePlayer());
            receiver.removeIncomingFriendRequest(sender.toOfflinePlayer());

            try {
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(receiver.getLanguageFile(), "Friends.Add.TimedOutFrom")).replaceAll(sender.toOfflinePlayer().getRankColor() + sender.getUsername());
                receiver.sendMessage(message);
            } catch (Exception e) {
            }

            try {
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(sender.getLanguageFile(), "Friends.Add.TimedOutTo")).replaceAll(receiver.toOfflinePlayer().getRankColor() + receiver.getUsername());
                sender.sendMessage(new TextComponent(message));
            } catch (Exception e) {
            }

        }, time, TimeUnit.MINUTES).getId();
    }
}
