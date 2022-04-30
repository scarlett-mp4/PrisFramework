package net.prismc.prishub.listeners;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.prishub.PrisHub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public class JoinListener implements Listener {

    @EventHandler
    public void onBukkitJoin(PlayerJoinEvent e) {
        try {
            Player p = e.getPlayer();
            Random random = new Random();
            int randomInt = random.ints(0, PrisHub.getInstance().spawnPointsList.size()).findFirst().getAsInt();
            p.teleport(PrisHub.getInstance().spawnPointsList.get(randomInt));
        } catch (Exception ignored) {
        }

        Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> {
            if (e.getPlayer().isOnline()) {
                PrisHub.getInstance().lobbyFunction.lobbyEnter(PrisCoreApi.wrapPrisBukkitPlayer(e.getPlayer()));
            }
        }, 20L);
    }
}
