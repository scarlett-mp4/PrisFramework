package net.prismc.prisbungeehandler.events.pris;

import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class PrisServerSwitchEvent extends Event {
    private final PrisPlayer player;
    private final Server server;

    public PrisServerSwitchEvent(PrisPlayer player, Server serverInfo) {
        this.player = player;
        this.server = serverInfo;
    }

    public PrisPlayer getPlayer() {
        return player;
    }

    public Server getServer() {
        return server;
    }

}
