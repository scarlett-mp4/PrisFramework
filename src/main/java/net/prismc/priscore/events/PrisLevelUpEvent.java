package net.prismc.priscore.events;

import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.LevelObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrisLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PrisBukkitPlayer player;
    private final LevelObject oldLevel;
    private final LevelObject newLevel;

    public PrisLevelUpEvent(PrisBukkitPlayer player, LevelObject oldLevel, LevelObject newLevel) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
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

    public LevelObject getOldLevel() {
        return oldLevel;
    }

    public LevelObject getNewLevel() {
        return newLevel;
    }
}
