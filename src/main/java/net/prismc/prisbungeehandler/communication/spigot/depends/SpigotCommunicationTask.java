package net.prismc.prisbungeehandler.communication.spigot.depends;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.CommunicationTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SpigotCommunicationTask extends CommunicationTask {
    protected SpigotCommunicationTask(String name) {
        super(name);
    }

    private void sendMessage(String message, ServerInfo server) {
        if (server == null)
            return;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
    }

    public void sendMessage(JsonObject object, PrisPlayer player) {
        object.addProperty("username", player.getUsername());
        object.addProperty("uuid", player.getUniqueId().toString());
        sendMessage(object.toString(), player.getServer().getInfo());
    }

    public void sendMessage(JsonObject object, String server) {
        sendMessage(object.toString(), ProxyServer.getInstance().getServerInfo(server));
    }
}
