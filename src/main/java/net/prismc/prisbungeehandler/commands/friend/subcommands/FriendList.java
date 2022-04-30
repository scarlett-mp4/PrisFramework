package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FriendList extends SubCommand {

    @Override
    public List<String> getNames() {
        return Arrays.asList("list", "l");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {

        if (args.length > 1) {
            try {
                int page = Integer.parseInt(args[1]);
                displayList(p, page);
            } catch (Exception e) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.List.NotAPage"));
            }
        } else {
            displayList(p, 1);
        }
    }

    private void displayList(PrisPlayer p, int page) {
        PrisApi api = new PrisApi();

        // Friends list is empty
        if (p.getFriendCache().size() < 1) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.General.Spacer"));
            UtilApi.sendCenteredMessage(p, UtilApi.getString(p.getLanguageFile(), "Friends.List.NoFriends"));
            UtilApi.sendCenteredMessage(p, UtilApi.getString(p.getLanguageFile(), "Friends.List.UwU"));
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.General.Spacer"));
            return;
        }

        // Creates friend list
        ArrayList<OfflinePrisPlayer> offlinePlayers = new ArrayList<>();
        ArrayList<OfflinePrisPlayer> onlinePlayers = new ArrayList<>();
        for (OfflinePrisPlayer offlinePrisPlayer : p.getFriendCache()) {
            try {
                if (PrisBungeeHandler.getInstance().getProxy().getPlayer(offlinePrisPlayer.getUniqueId()).isConnected()) {
                    onlinePlayers.add(offlinePrisPlayer);
                } else {
                    offlinePlayers.add(offlinePrisPlayer);
                }
            } catch (Exception e) {
                offlinePlayers.add(offlinePrisPlayer);
            }
        }
        offlinePlayers.sort(Comparator.comparing(OfflinePrisPlayer::getUsername));
        onlinePlayers.sort(Comparator.comparing(OfflinePrisPlayer::getUsername));
        ArrayList<OfflinePrisPlayer> friendsList = new ArrayList<>(onlinePlayers);
        friendsList.addAll(offlinePlayers);
        List<OfflinePrisPlayer> friendsOnPage;
        int size = 6;
        int toIndex = page * size;
        int fromIndex = toIndex - size;
        try {
            friendsOnPage = friendsList.subList(fromIndex, toIndex);
        } catch (IndexOutOfBoundsException e) {
            friendsOnPage = friendsList.subList(fromIndex, friendsList.size());
        }

        // Finds the amount of pages and cancels command if overloaded
        int maxPages = (int) Math.ceil((double) friendsList.size() / (double) size);
        if (page > maxPages) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.List.NotAPage"));
            return;
        }

        // Displays menu bar
        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.General.Spacer"));
        TextComponent left = new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.List.LeftArrow"));
        TextComponent right = new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.List.RightArrow"));
        TextComponent noLeft = new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.List.NoLeft"));
        TextComponent noRight = new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.List.NoRight"));
        TextComponent header = new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.List.Header"));
        String next = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Friends.List.HoverPage")).replaceAll(String.valueOf(page + 1));
        String back = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Friends.List.HoverPage")).replaceAll(String.valueOf(page - 1));
        String currentPage = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Friends.List.CurrentPage")).replaceAll(String.valueOf(page));
        currentPage = PatternCollection.MAXPAGES_PATTERN.matcher(currentPage).replaceAll(String.valueOf(maxPages));
        left.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(back)));
        left.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend list " + (page - 1)));
        right.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(next)));
        right.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend list " + (page + 1)));
        header.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("")));
        header.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ""));
        noLeft.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ""));
        noRight.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ""));
        ComponentBuilder builder = new ComponentBuilder().append(left).append(" ").append(header).append(" ").append(right);

        if (page == 1) {
            if (page == maxPages) {
                builder = new ComponentBuilder().append(noLeft).append(" ").append(header).append(" ").append(noRight);
            } else {
                builder = new ComponentBuilder().append(noLeft).append(" ").append(header).append(" ").append(right);
            }
        } else if (page == maxPages) {
            builder = new ComponentBuilder().append(left).append(" ").append(header).append(" ").append(noRight);
        }
        UtilApi.sendCenteredMessage(p, builder);
        p.sendMessage(new TextComponent(currentPage));

        // Displays friends
        for (OfflinePrisPlayer friend : friendsOnPage) {
            if (onlinePlayers.contains(friend)) {
                PrisPlayer onlineFriend = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(friend.getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Friends.List.OnlineMessage")).replaceAll(friend.getRankColor() + friend.getUsername());
                String serverName;
                try {
                    serverName = onlineFriend.getServer().getInfo().getName();
                } catch (Exception e) {
                    serverName = "connecting";
                }
                String message1;
                if (serverName.contains("lobby") || serverName.contains("connecting")) {
                    message1 = PatternCollection.STATUS_PATTERN.matcher(message).replaceAll(UtilApi.getString(p.getLanguageFile(), "Friends.List.Status.Lobby"));
                } else {
                    message1 = PatternCollection.STATUS_PATTERN.matcher(message).replaceAll(UtilApi.getString(p.getLanguageFile(), "Friends.List.Status.RPG"));
                }
                p.sendMessage(message1);
            } else {
                String offlineMessage = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Friends.List.OfflineMessage")).replaceAll(friend.getRankColor() + friend.getUsername());
                p.sendMessage(offlineMessage);
            }
        }
        if (friendsOnPage.size() != size) {
            for (int i = friendsOnPage.size(); i < size; i++) {
                p.sendMessage(new TextComponent(" "));
            }
        }
        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.General.Spacer"));
    }
}
