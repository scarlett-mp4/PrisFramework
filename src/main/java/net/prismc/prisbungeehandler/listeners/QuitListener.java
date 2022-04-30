package net.prismc.prisbungeehandler.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.communication.sql.PrisSQL;
import net.prismc.prisbungeehandler.events.pris.PrisQuitEvent;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;

import java.sql.SQLException;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        try {
            if (e.getPlayer().getServer().getInfo() != null) {
                PrisApi api = new PrisApi();
                PrisPlayer p = api.wrapPlayer(e.getPlayer());

                PrisBungeeHandler instance = PrisBungeeHandler.getInstance();
                PrisSQL prisSQL = new PrisSQL();

                ProxyServer.getInstance().getScheduler().runAsync(instance, () -> {
                    try {
                        LocalDateTime sessionStartTime = p.getSessionStartTime();
                        LocalDateTime sessionQuitTime = LocalDateTime.now();
                        int oldTime = Seconds.secondsBetween(sessionStartTime, sessionQuitTime).getSeconds();
                        p.toOfflinePlayer().setPlayTime(p.toOfflinePlayer().getPlayTime() + oldTime, false);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        try {
                            prisSQL.updateData(p);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } finally {
                            p.alertFriendsOffline();
                            p.quit();
                        }
                    }
                });

                ProxyServer.getInstance().getPluginManager().callEvent(new PrisQuitEvent(p));
            }
        } catch (Exception exception) {
            ProxyServer.getInstance().getLogger().severe(e.getPlayer().getName() + " triggered a logout error.");
        }
    }
}
