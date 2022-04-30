package net.prismc.priscore.commands;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Health extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (p.hasPermission("prismc.health")) {
                if (args.length == 0) {
                    p.sendMessage(getString(lang, "Commands.InvalidArgs"));

                } else if (args.length == 1) {
                    try {
                        double i = Double.parseDouble(args[0]);
                        AttributeInstance pHealth = p.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);

                        if (i >= 5 && i <= 2048) {
                            pHealth.setBaseValue(i);
                            p.setHealthScale(i);
                            p.getPlayer().setHealth(i);
                            p.sendMessage(getString(lang, "Commands.Health.Updated"));
                        } else {
                            p.sendMessage(getString(lang, "Commands.Health.FiveThroughTwentyFortyEight"));
                        }
                    } catch (NumberFormatException e) {
                        p.sendMessage(getString(lang, "Commands.InvalidArgs"));
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
