package net.prismc.prisbungeehandler.communication.spigot.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Server;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.communication.spigot.depends.SpigotTask;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class GetFriendList extends SpigotTask {

    public GetFriendList() {
        super("getfriendlist");
    }

    @Override
    public void executeTask(PrisPlayer p, Server server, JsonObject object) {
        JsonObject toSend = new JsonObject();
        JsonArray friends = new JsonArray();
        PrisApi api = new PrisApi();

        for (OfflinePrisPlayer offlinePrisPlayer : p.getFriendCache()) {
            JsonObject jsonFriend = new JsonObject();

            try {
                PrisPlayer friend = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                jsonFriend.addProperty("uuid", offlinePrisPlayer.getUniqueId().toString());
                jsonFriend.addProperty("name", offlinePrisPlayer.getUsername());
                jsonFriend.addProperty("lastseen", offlinePrisPlayer.getLastOnline());
                jsonFriend.addProperty("firstjoin", offlinePrisPlayer.getFirstJoin());
                jsonFriend.addProperty("servername", friend.getServer().getInfo().getName());
            } catch (Exception e) {
                jsonFriend.addProperty("uuid", offlinePrisPlayer.getUniqueId().toString());
                jsonFriend.addProperty("name", offlinePrisPlayer.getUsername());
                jsonFriend.addProperty("lastseen", offlinePrisPlayer.getLastOnline());
                jsonFriend.addProperty("firstjoin", offlinePrisPlayer.getFirstJoin());
                jsonFriend.addProperty("servername", "OFFLINE");
            }
            friends.add(jsonFriend);
        }

        toSend.addProperty("subchannel", "getfriendlist");
        toSend.add("friends", friends);
        sendMessage(toSend, p);
    }
}
