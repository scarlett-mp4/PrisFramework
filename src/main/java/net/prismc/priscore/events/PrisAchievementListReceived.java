package net.prismc.priscore.events;

import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrisAchievementListReceived extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final JsonObject object;

    public PrisAchievementListReceived(JsonObject object) {
        this.object = object;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public JsonObject getObject() {
        return object;
    }
}