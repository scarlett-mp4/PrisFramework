package net.prismc.prisbungeehandler.events.pris;

import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class PrisJoinEvent extends Event {

    private final PrisPlayer player;

    public PrisJoinEvent(PrisPlayer player) {
        this.player = player;
    }

    public PrisPlayer getPlayer() {
        return player;
    }
}
