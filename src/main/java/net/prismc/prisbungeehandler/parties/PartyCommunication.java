package net.prismc.prisbungeehandler.parties;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PartyCommunication {

    public static void update(PrisParty party) {
        for (OfflinePrisPlayer p : party.getUsers().keySet()) {
            ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
                try {
                    JsonObject partyObject = new JsonObject();
                    JsonObject toSend = new JsonObject();
                    JsonArray users = new JsonArray();

                    for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                        JsonObject jsonMember = new JsonObject();
                        jsonMember.addProperty("name", offlinePrisPlayer.getUsername());
                        jsonMember.addProperty("uuid", String.valueOf(offlinePrisPlayer.getUniqueId()));
                        jsonMember.addProperty("id", offlinePrisPlayer.getID());
                        jsonMember.addProperty("role", party.getRole(offlinePrisPlayer));
                        jsonMember.addProperty("online", offlinePrisPlayer.isOnline());
                        users.add(jsonMember);
                    }

                    partyObject.add("members", users);
                    partyObject.addProperty("open", party.isOpen());

                    toSend.addProperty("uuid", p.getUniqueId().toString());
                    toSend.addProperty("subchannel", "partyupdate");
                    toSend.add("party", partyObject);
                    send(toSend, new PrisApi().wrapPlayer(ProxyServer.getInstance().getPlayer(p.getUniqueId())));

                } catch (Exception ignore) {
                }
            });
        }
    }

    private static void send(JsonObject toSend, PrisPlayer player) {
        if (player.getServer().getInfo() == null)
            return;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(toSend.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.getServer().getInfo().sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
    }
}
