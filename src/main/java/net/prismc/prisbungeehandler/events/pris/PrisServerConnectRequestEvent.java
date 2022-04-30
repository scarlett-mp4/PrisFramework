package net.prismc.prisbungeehandler.events.pris;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class PrisServerConnectRequestEvent extends Event {
    private final PrisPlayer player;
    private final ServerConnectEvent serverConnectEvent;

    public PrisServerConnectRequestEvent(PrisPlayer player, ServerConnectEvent serverConnectEvent) {
        this.player = player;
        this.serverConnectEvent = serverConnectEvent;
    }

    public PrisPlayer getPlayer() {
        return player;
    }

    public ServerInfo getTarget() {
        return serverConnectEvent.getTarget();
    }

    public void setTarget(ServerInfo value) {
        serverConnectEvent.setTarget(value);
    }

    public boolean isCanceled() {
        return serverConnectEvent.isCancelled();
    }

    public void setCancelled(boolean value) {
        serverConnectEvent.setCancelled(value);
    }

    public ServerConnectEvent.Reason getReason() {
        return serverConnectEvent.getReason();
    }
}
