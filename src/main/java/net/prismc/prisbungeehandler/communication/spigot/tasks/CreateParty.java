package net.prismc.prisbungeehandler.communication.spigot.tasks;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Server;
import net.prismc.prisbungeehandler.communication.spigot.depends.SpigotTask;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class CreateParty extends SpigotTask {

    public CreateParty() {
        super("createparty");
    }

    @Override
    public void executeTask(PrisPlayer p, Server server, JsonObject object) {

        if (object.get("open").getAsBoolean()) {
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getPlayer(p.getUniqueId()), "party create open");
        } else {
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getPlayer(p.getUniqueId()), "party create closed");
        }
    }
}
