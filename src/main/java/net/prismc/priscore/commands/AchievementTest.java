package net.prismc.priscore.commands;

import net.md_5.bungee.api.ChatColor;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;

public class AchievementTest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            p.sendMessage(ChatColor.GREEN + "Executed.");

            if (p.hasPermission("prismc.admin")) {
                new BukkitRunnable() {
                    final Location loc = p.getLocation().subtract(0, 1, 0);
                    double t = 0;

                    @Override
                    public void run() {

                        new ParticleBuilder(ParticleEffect.REDSTONE, loc.add(0, 0.75, 0))
                                .setColor(Color.decode(PrisCore.getInstance().getConfig().getString("Test")))
                                .setAmount(50)
                                .setOffsetX((float) t)
                                .setOffsetY(0.5F)
                                .setOffsetZ((float) t)
                                .setSpeed(0F)
                                .display(Bukkit.getOnlinePlayers());
                        t = t + 1;

                        if (t >= 6.0)
                            cancel();
                    }
                }.runTaskTimer(PrisCore.getInstance(), 0, 1);
            }
        }
        return true;
    }
}