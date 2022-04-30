package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class FriendRemove extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("remove");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisApi api = new PrisApi();

        if (args.length > 1) {
            try {
                OfflinePrisPlayer friend = api.wrapOfflinePlayer(args[1]);
                if (p.containsFriendCache(friend)) {
                    p.sendMessage(PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(),
                            "Friends.Remove.FrenemyRemoved")).replaceAll(friend.getRankColor() + friend.getUsername()));
                    try {
                        PrisPlayer onlineFriend = api.wrapPlayer(ProxyServer.getInstance().getPlayer(friend.getUniqueId()));
                        onlineFriend.sendMessage(PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(onlineFriend.getLanguageFile(),
                                "Friends.Remove.BFFRemoved")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername()));
                    } catch (Exception ignored) {
                    }
                    p.toOfflinePlayer().removeFriend(friend);
                } else {
                    p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.Remove.NotFriends")));
                }
            } catch (Exception e) {
                p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.Remove.NotFriends")));
            }
        } else {
            p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.General.MissingArgs")));
        }
    }
}
