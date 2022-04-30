package net.prismc.priscore.events;

import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;

public class PrisQuitEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PrisBukkitPlayer player;
    private final PlayerQuitEvent e;

    public PrisQuitEvent(PrisBukkitPlayer player, PlayerQuitEvent e) {
        this.player = player;
        this.e = e;
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

    public String getQuitMessage() {
        return e.getQuitMessage();
    }

    public void setQuitMessage(String value) {
        e.setQuitMessage(value);
    }
}
