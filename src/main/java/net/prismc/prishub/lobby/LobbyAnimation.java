package net.prismc.prishub.lobby;

import net.prismc.priscore.PrisCore;
import net.prismc.prishub.PrisHub;
import net.prismc.prishub.utils.FontInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.concurrent.atomic.AtomicLong;

public class LobbyAnimation {

    public static void playAnimation(Player p) {
        String raw = PrisHub.getInstance().getConfig().getString("Animation.Subtitle").toLowerCase();
        Long delay = PrisHub.getInstance().getConfig().getLong("Animation.FrameDelay");
        StringBuilder builder = new StringBuilder();

        boolean append = false;
        for (char c : raw.toCharArray()) {

            if (append) {
                append = false;
                if (c != ' ') {
                    builder.append("&").append(c);
                    continue;
                }
            }
            if (c == '&') {
                append = true;
                continue;
            }

            for (FontInfo info : FontInfo.values()) {
                if (c == info.getCharacter()) {
                    builder.append(info.getReplacement());
                }
            }
        }

        p.playSound(p.getLocation(), "custom.swoosh", 0.4F, 1.6F);
        int frames = 81;
        int ch = 59648;
        for (int i = 1; i <= frames; i++) {
            int finalCh = ch;
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> {
                if (p.isOnline()) {
                    p.sendTitle(String.valueOf((char) finalCh), "", 0, 60, 0);
                    if (finalI == frames) {
                        Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> {
                            subtitle(p, builder.toString(), String.valueOf((char) finalCh));
                        }, 15);
                    }
                }
            }, i / delay);
            ch++;
        }
    }

    private static void subtitle(Player p, String sub, String title) {
        StringBuilder builder = new StringBuilder();
        boolean skip = false;
        AtomicLong time = new AtomicLong();
        long delay = PrisHub.getInstance().getConfig().getLong("Animation.Delay");

        for (int i = 0; i < sub.length(); i++) {
            String s = sub.substring(i, i + 1);
            if (s.equals("\uE827")) {
                builder.append("\uE827");
                continue;
            }
            if (s.equals("&")) {
                skip = true;
                continue;
            }
            if (skip) {
                builder.append("§").append(s);
                skip = false;
                continue;
            }
            builder.append(s);

            String send = builder.toString();
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> {
                p.sendTitle(title, send, 0, 60, 30);
                p.playSound(p.getLocation(), "custom.text", 2, 1);
                if (finalI == sub.length() - 1) {
                    end(p, send, title);
                }
            }, time.get() * delay);

            time.getAndIncrement();
        }
    }

    private static void end(Player p, String sub, String title) {
        Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> {
            p.playSound(p.getLocation(), "custom.end", 1, 1);
            p.sendTitle(title, ChatColor.translateAlternateColorCodes('&', "&a&l") + ChatColor.stripColor(sub), 0, 40, 30);

            new BukkitRunnable() {
                final Location loc = p.getLocation().subtract(0, 1, 0);
                double t = 0;

                @Override
                public void run() {
                    new ParticleBuilder(ParticleEffect.FIREWORKS_SPARK, loc.add(0, 0.75, 0))
                            .setAmount(50)
                            .setOffsetX((float) t)
                            .setOffsetY(0.5F)
                            .setOffsetZ((float) t)
                            .setSpeed(0.03F)
                            .display(p.getPlayer());
                    t = t + 1;

                    if (t >= 10.0)
                        cancel();
                }
            }.runTaskTimer(PrisCore.getInstance(), 0, 1);
        }, 15L);
    }

}
