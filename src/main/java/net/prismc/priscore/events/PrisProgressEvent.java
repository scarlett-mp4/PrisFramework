package net.prismc.priscore.events;

import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.LevelObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrisProgressEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PrisBukkitPlayer player;
    private final LevelObject currentLevel;

    public PrisProgressEvent(PrisBukkitPlayer player, LevelObject currentLevel) {
        this.player = player;
        this.currentLevel = currentLevel;
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

    public LevelObject getCurrentLevel() {
        return currentLevel;
    }
}
