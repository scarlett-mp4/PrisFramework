package net.prismc.priscore.listeners;

import net.prismc.priscore.events.PrisProgressEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelListener implements Listener {

    @EventHandler
    private void onPrisProgress(PrisProgressEvent e) {
        if (e.getPlayer().isLevelBarDisplayed()) {
            e.getPlayer().createLevelBar();
        }
    }

}
