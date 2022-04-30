package net.prismc.priscore.achievements;

import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.events.PrisAchievementEvent;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;

public class AchievementListener implements Listener {

    @EventHandler
    public void onAchievement(PrisAchievementEvent e) {
        PrisBukkitPlayer p = e.getPlayer();
        String hex = "#ffffff";

        p.sendTitle(UtilApi.getString(p.getLanguageFile(), "Achievements.Listing." + e.getAchievement() + ".Name"), UtilApi.getString(p.getLanguageFile(),
                "Achievements.General.TitleComplete"), 5, 60, 20);
        String s = UtilApi.getString(p.getLanguageFile(), "Achievements.Listing." + e.getAchievement() + ".Name");
        s = ChatColor.stripColor(s);
        s = ChatColor.GOLD + s;
        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Achievements.General.Spacer"));
        p.sendMessage(UtilApi.centerMessage(UtilApi.getString(p.getLanguageFile(), "Achievements.General.TextComplete")));
        switch (e.getRarity().toLowerCase()) {
            case "common":
                hex = "#b0b0b0";
                p.playSound(p.getLocation(), "custom.common", 2F, 1F);
                p.sendMessage(UtilApi.centerMessage(s + " &8- " + UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities.Common")));
                break;
            case "uncommon":
                hex = "#bbfca7";
                p.playSound(p.getLocation(), "custom.uncommon", 2F, 1F);
                p.sendMessage(UtilApi.centerMessage(s + " &8- " + UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities.Uncommon")));
                break;
            case "rare":
                hex = "#91fff8";
                p.playSound(p.getLocation(), "custom.rare", 2F, 1F);
                p.sendMessage(UtilApi.centerMessage(s + " &8- " + UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities.Rare")));
                break;
            case "epic":
                hex = "#ffa8fc";
                p.playSound(p.getLocation(), "custom.epic", 2F, 1F);
                p.sendMessage(UtilApi.centerMessage(s + " &8- " + UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities.Epic")));
                break;
            case "legendary":
                hex = "#ff7575";
                p.playSound(p.getLocation(), "custom.legendary", 2F, 1F);
                p.sendMessage(UtilApi.centerMessage(s + " &8- " + UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities.Legendary")));
                break;
        }
        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Achievements.General.Spacer"));

        String finalHex = hex;
        new BukkitRunnable() {
            final Location loc = p.getLocation().subtract(0, 1, 0);
            double t = 0;

            @Override
            public void run() {

                new ParticleBuilder(ParticleEffect.REDSTONE, loc.add(0, 0.75, 0))
                        .setColor(Color.decode(finalHex))
                        .setAmount(50)
                        .setOffsetX((float) t)
                        .setOffsetY(0.5F)
                        .setOffsetZ((float) t)
                        .setSpeed(0F)
                        .display(Bukkit.getOnlinePlayers());
                t = t + 1;

                if (t >= 5.0)
                    cancel();
            }
        }.runTaskTimer(PrisCore.getInstance(), 0, 1);
    }
}
