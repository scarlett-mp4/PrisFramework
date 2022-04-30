package net.prismc.priscore.events;

import com.google.gson.JsonObject;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrisAchievementEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PrisBukkitPlayer player;
    private final String achievement;
    private final String category;
    private final String rarity;

    public PrisAchievementEvent(PrisBukkitPlayer player, JsonObject object) {
        this.player = player;
        this.achievement = object.get("achievement").getAsString();
        this.category = object.get("category").getAsString();
        this.rarity = object.get("rarity").getAsString();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public PrisBukkitPlayer getPlayer() {
        return this.player;
    }

    public String getAchievement() {
        return achievement;
    }

    public String getCategory() {
        return category;
    }

    public String getRarity() {
        return rarity;
    }
}
