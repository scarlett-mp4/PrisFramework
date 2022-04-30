package net.prismc.prisbungeehandler.communication.spigot.depends;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.connection.Server;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public abstract class SpigotTask extends SpigotCommunicationTask {

    protected SpigotTask(String channel) {
        super(channel);
    }

    public abstract void executeTask(PrisPlayer player, Server server, JsonObject object);

}
