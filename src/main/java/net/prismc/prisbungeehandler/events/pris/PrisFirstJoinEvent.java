package net.prismc.prisbungeehandler.events.pris;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class PrisFirstJoinEvent extends Event {
    private final ProxiedPlayer player;

    public PrisFirstJoinEvent(ProxiedPlayer player) {
        this.player = player;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

}
