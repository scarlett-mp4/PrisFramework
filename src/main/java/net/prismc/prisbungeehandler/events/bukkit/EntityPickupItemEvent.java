package net.prismc.prisbungeehandler.events.bukkit;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class EntityPickupItemEvent extends Event {
    private final PrisPlayer player;
    private final JsonObject object;

    public EntityPickupItemEvent(PrisPlayer player, JsonObject object) {
        this.player = player;
        this.object = object;
    }

    public PrisPlayer getPlayer() {
        return player;
    }

    public String getPickedItem() {
        return object.get("itemPickup").getAsString();
    }

    public String getItemLocation() {
        return object.get("itemLocation").getAsString();
    }
}
