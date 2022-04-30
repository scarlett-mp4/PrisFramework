package net.prismc.priscore.listeners;

import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.events.PrisJoinEvent;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    /**
     * Handles the player join event
     */
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");

        Bukkit.getScheduler().runTaskLater(PrisCore.getInstance(), () -> {
            try {
                PrisBukkitPlayer player = PrisCoreApi.wrapPrisBukkitPlayer(e.getPlayer());
                Bukkit.getPluginManager().callEvent(new PrisJoinEvent(player));
            } catch (Exception exception) {
                e.getPlayer().kickPlayer("Error gathering player data. Please retry!");
            }
        }, 10L);
    }

    @EventHandler
    public void onJoinEvent(AsyncPlayerPreLoginEvent e) {
        if (!PrisCore.getInstance().isReady) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.LIGHT_PURPLE + "Pris is currently enabling this server!");
        }
    }
}
