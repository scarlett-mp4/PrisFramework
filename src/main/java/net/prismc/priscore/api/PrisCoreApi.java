package net.prismc.priscore.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.files.EngConfiguration;
import net.prismc.priscore.files.FreConfiguration;
import net.prismc.priscore.files.GerConfiguration;
import net.prismc.priscore.files.SpaConfiguration;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.prisplayer.PrisFriend;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class PrisCoreApi {

    public static PrisBukkitPlayer wrapPrisBukkitPlayer(Player p) {
        final PrisCore instance = PrisCore.getInstance();
        return instance.cache.get(p.getUniqueId());
    }

    public static boolean getServerStatus(String ip, int port) {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 10);
            s.close();
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    public static EngConfiguration getEnglish() {
        return PrisCore.getInstance().engConfiguration;
    }

    public static SpaConfiguration getSpanish() {
        return PrisCore.getInstance().spaConfiguration;
    }

    public static FreConfiguration getFrench() {
        return PrisCore.getInstance().freConfiguration;
    }

    public static GerConfiguration getGerman() {
        return PrisCore.getInstance().gerConfiguration;
    }

    public static ArrayList<PrisFriend> getFriends(JsonObject object) {
        ArrayList<PrisFriend> onFriends = new ArrayList<>();
        ArrayList<PrisFriend> offFriends = new ArrayList<>();
        ArrayList<PrisFriend> friends = new ArrayList<>();

        JsonArray array = object.getAsJsonArray("friends");
        for (int i = 0; i < array.size(); i++) {
            JsonObject friendObject = array.get(i).getAsJsonObject();
            PrisFriend friend = new PrisFriend(friendObject.get("name").getAsString(), friendObject.get("firstjoin").getAsString(), friendObject.get("lastseen").getAsString(),
                    friendObject.get("servername").getAsString(), UUID.fromString(friendObject.get("uuid").getAsString()));
            if (friend.isOnline()) {
                onFriends.add(friend);
            } else {
                offFriends.add(friend);
            }
        }
        onFriends.sort(Comparator.comparing(PrisFriend::getName));
        offFriends.sort(Comparator.comparing(PrisFriend::getName));
        friends.addAll(onFriends);
        friends.addAll(offFriends);

        return friends;
    }

    public void updateLanguage(Player p, String newLanguage) {
        JsonObject object = new JsonObject();
        object.addProperty("SubChannel", "LanguageUpdate");
        object.addProperty("PlayerUUID", p.getUniqueId().toString());
        object.addProperty("PlayerName", p.getName());
        object.addProperty("Language", newLanguage);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(PrisCore.getInstance(), PrisCore.BUNGEE_CHANNEL, stream.toByteArray());
    }
}
