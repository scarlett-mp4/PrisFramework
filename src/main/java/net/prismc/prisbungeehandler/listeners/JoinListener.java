package net.prismc.prisbungeehandler.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.communication.spigot.PrisPluginMessage;
import net.prismc.prisbungeehandler.communication.sql.PrisSQL;
import net.prismc.prisbungeehandler.communication.sql.SQL;
import net.prismc.prisbungeehandler.events.pris.PrisJoinEvent;
import net.prismc.prisbungeehandler.events.pris.PrisServerConnectRequestEvent;
import net.prismc.prisbungeehandler.events.pris.PrisServerSwitchEvent;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.sql.SQLException;

public class JoinListener implements Listener {

    private final PrisSQL prisSQL = new PrisSQL();
    private final PrisPluginMessage prisPluginMessage = new PrisPluginMessage();
    private final SQL sql = new SQL();
    private final PrisApi api = new PrisApi();

    @EventHandler
    public void onProxyJoin(PostLoginEvent e) {
        ProxiedPlayer proxiedPlayer = e.getPlayer();

        try {
            if (!sql.exists("uuid", proxiedPlayer.getUniqueId().toString(), "pris_players")) {
                PrisBungeeHandler.getInstance().getProxy().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
                    try {
                        prisSQL.firstJoin(proxiedPlayer);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @EventHandler
    public void onConnectRequest(ServerConnectEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if (!e.getRequest().isRetry()) {
            ProxyServer.getInstance().getPluginManager().callEvent(new PrisServerConnectRequestEvent(new PrisApi().wrapPlayer(p), e));
        }
    }

    @EventHandler
    public void onServerJoin(ServerSwitchEvent e) {
        try {
            PrisPlayer prisPlayer = api.wrapPlayer(e.getPlayer());
            prisSQL.anyJoin(prisPlayer);
            prisPluginMessage.createPrisBukkitPlayer(prisPlayer);

            if (prisPlayer.isNewSession()) {
                prisPlayer.setNewSession(false);

                try {
                    ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
                        try {
                            for (String s : prisSQL.getFriends(prisPlayer.toOfflinePlayer())) {
                                OfflinePrisPlayer friend = api.wrapOfflinePlayer(Integer.parseInt(s));
                                prisPlayer.addFriendCache(friend);
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        prisPlayer.alertFriendsOnline();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                ProxyServer.getInstance().getPluginManager().callEvent(new PrisJoinEvent(prisPlayer));
            } else {
                ProxyServer.getInstance().getPluginManager().callEvent(new PrisServerSwitchEvent(prisPlayer, prisPlayer.getServer()));
            }

            PrisApi.achievementCheckAndExecute(prisPlayer);

        } catch (Exception exception) {
            ProxyServer.getInstance().getLogger().severe(e.getPlayer().getName() + " could not create PrisPlayer!");
            e.getPlayer().disconnect(new TextComponent(ChatColor.GOLD + "Error generating player profile! Please try reconnecting."));
            exception.printStackTrace();
        }
    }
}
