package net.prismc.prisbungeehandler.communication.spigot.tasks;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Server;
import net.prismc.prisbungeehandler.communication.spigot.depends.SpigotTask;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class JoinParty extends SpigotTask {

    public JoinParty() {
        super("joinparty");
    }

    @Override
    public void executeTask(PrisPlayer p, Server server, JsonObject object) {
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getPlayer(p.getUniqueId()), "party join " + object.get("partyname").getAsString());
    }
}
