package net.prismc.prisbungeehandler.listeners;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.events.pris.PrisAchievementEvent;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AchievementListener implements Listener {

    @EventHandler
    public void onAchievement(PrisAchievementEvent e) {
        PrisPlayer p = e.getPlayer();
        AchievementHandler achievementHandler = e.getAchievement();

        ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
            JsonObject toSend = new JsonObject();
            toSend.addProperty("subchannel", "achievementevent");
            toSend.addProperty("username", p.getUsername());
            toSend.addProperty("uuid", p.getUniqueId().toString());
            toSend.addProperty("id", p.toOfflinePlayer().getID());
            toSend.addProperty("achievement", achievementHandler.getName());
            toSend.addProperty("category", achievementHandler.getType().getCategory());
            toSend.addProperty("rarity", achievementHandler.getRarity().getRarity());

            if (p.getServer().getInfo() == null)
                return;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF(toSend.toString());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            p.getServer().getInfo().sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
            p.addCompletedAchievement(achievementHandler.getName());
        });
    }

}
