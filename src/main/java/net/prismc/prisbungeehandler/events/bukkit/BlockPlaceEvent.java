package net.prismc.prisbungeehandler.events.bukkit;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class BlockPlaceEvent extends Event {
    private final PrisPlayer player;
    private final JsonObject object;

    public BlockPlaceEvent(PrisPlayer player, JsonObject object) {
        this.player = player;
        this.object = object;
    }

    public PrisPlayer getPlayer() {
        return player;
    }

    public String getBlock() {
        return object.get("block").getAsString();
    }

    public String getLocation() {
        return object.get("blockLocation").getAsString();
    }
}
