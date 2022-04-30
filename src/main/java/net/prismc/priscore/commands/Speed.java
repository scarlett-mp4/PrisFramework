package net.prismc.priscore.commands;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Speed extends UtilApi implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (sender.hasPermission("prismc.speed")) {
                if (args.length == 0) {
                    p.getInventory().clear();
                    p.sendMessage(getString(lang, "Commands.InvalidArgs"));

                } else if (args.length == 1) {
                    try {
                        int i = Integer.parseInt(args[0]);
                        if (i >= 0 && i <= 10) {
                            if (p.isFlying()) {
                                p.setFlySpeed((float) Integer.parseInt(args[0]) / 10);
                                p.sendMessage(getString(lang, "Commands.Speed.FlySpeed"));
                            } else {
                                p.setWalkSpeed((float) Integer.parseInt(args[0]) / 10);
                                p.sendMessage(getString(lang, "Commands.Speed.WalkSpeed"));
                            }
                        } else {
                            p.sendMessage(getString(lang, "Commands.Speed.ZeroThroughTen"));
                        }
                    } catch (Exception e) {
                        p.sendMessage(getString(lang, "Commands.Speed.ZeroThroughTen"));
                    }
                }

            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[PrisLobby]" + ChatColor.YELLOW + " [Utils] " + ChatColor.RED + "Only players can execute this command!");
        }
        return true;
    }
}
