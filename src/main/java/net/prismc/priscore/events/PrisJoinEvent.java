package net.prismc.priscore.events;

import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrisJoinEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PrisBukkitPlayer player;

    public PrisJoinEvent(PrisBukkitPlayer player) {
        this.player = player;
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
}
