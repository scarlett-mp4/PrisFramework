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

import static net.prismc.priscore.utils.PatternCollection.PLAYER_PATTERN;

public class Clear extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();
            if (sender.hasPermission("prismc.clear")) {
                if (args.length == 0) {
                    p.getInventory().clear();
                    p.sendMessage(getString(lang, "Commands.Clear.Cleared"));
                } else if (args.length == 1 && p.hasPermission("prismc.clear.others")) {
                    PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));

                    try {
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Clear.Cleared"));
                        t.getInventory().clear();
                        String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Clear.ClearedOther")).replaceAll(t.getName());
                        p.sendMessage(message);
                    } catch (Exception var9) {
                        p.sendMessage(getString(lang, "Commands.PlayerNotOnline"));
                    }
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            if (args.length == 0) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! Missing Arguments.");
            } else if (args.length == 1) {
                PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                try {
                    t.sendMessage(getString(t.getLanguageFile(), "Commands.Clear.Cleared"));
                    t.getInventory().clear();
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&dYou have cleared &a") + args[0] + ChatColor.LIGHT_PURPLE + "'s inventory!");
                } catch (Exception var9) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is currently not online."));
                }
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! Too many arguments.");
            }
        }

        return true;
    }
}
