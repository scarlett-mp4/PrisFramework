package net.prismc.priscore.listeners;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.events.PrisQuitEvent;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    /**
     * Handles the player quit event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("");

        try {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer(e.getPlayer());
            Bukkit.getPluginManager().callEvent(new PrisQuitEvent(p, e));
            p.quit();
        } catch (Exception exception) {
            Bukkit.getLogger().severe("Could not retrieve PrisBukkitPlayer on PlayerQuitEvent!");
        }
    }
}
