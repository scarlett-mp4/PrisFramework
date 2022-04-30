package net.prismc.prisbungeehandler.communication.spigot.tasks;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.connection.Server;
import net.prismc.prisbungeehandler.communication.spigot.depends.SpigotTask;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class PlayerUpdate extends SpigotTask {

    public PlayerUpdate() {
        super("bukkitplayerupdate");
    }

    @Override
    public void executeTask(PrisPlayer p, Server server, JsonObject object) {
        p.toOfflinePlayer().setLang(object.get("lang").getAsString(), false);
        p.toOfflinePlayer().setSettings(object.get("settings").getAsString(), false);
        p.toOfflinePlayer().setPriscoins(object.get("priscoins").getAsInt(), false);
        p.toOfflinePlayer().setRubies(object.get("rubies").getAsInt(), false);
        p.toOfflinePlayer().setKarma(object.get("karma").getAsInt(), false);
    }
}
