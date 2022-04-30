package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class FriendDeny extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("deny");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        PrisApi api = new PrisApi();

        try {
            if (args.length > 1) {
                if (p.containsIncomingFriendRequest(api.wrapOfflinePlayer(args[1]))) {
                    denyFriendRequest(p, api.wrapOfflinePlayer(args[1]));
                } else {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Accept.NoRequest"));
                }
            } else {
                p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.General.MissingArgs")));
            }
        } catch (Exception e) {
            p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Friends.Deny.NoRequest")));
        }
    }

    public void denyFriendRequest(PrisPlayer p1, OfflinePrisPlayer p2) {
        PrisBungeeHandler instance = PrisBungeeHandler.getInstance();
        PrisApi api = new PrisApi();

        ProxyServer.getInstance().getScheduler().cancel(p1.getIncomingFriendRequestTask(p2));
        p1.toOfflinePlayer().removeFriend(p2);
        p1.removeIncomingFriendRequest(p2);

        ProxyServer.getInstance().getScheduler().runAsync(instance, () -> {
            try {
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p1.getLanguageFile(), "Friends.Deny.YouDeclined")).replaceAll(p2.getRankColor() + p2.getUsername());
                p1.sendMessage(message);
            } catch (Exception ignored) {
            }

            try {
                PrisPlayer player = api.wrapPlayer(ProxyServer.getInstance().getPlayer(p2.getUniqueId()));
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(player.getLanguageFile(), "Friends.Deny.GotDeclined")).replaceAll(p1.toOfflinePlayer().getRankColor() + p1.getUsername());
                player.sendMessage(message);
            } catch (Exception ignored) {
            }
        });
    }
}