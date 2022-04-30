package net.prismc.priscore.commands;

import net.prismc.priscore.PrisCore;
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

import static net.prismc.priscore.utils.PatternCollection.WARP_PATTERN;

public class Delwarp extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();
            if (p.hasPermission("prismc.delwarp")) {
                if (args.length == 0) {
                    p.sendMessage(getString(lang, "Commands.Delwarp.IncludeAWarp"));
                } else if (args.length == 1) {
                    String warpName = args[0];
                    if (PrisCore.getInstance().getConfig().contains("Warps." + warpName)) {
                        PrisCore.getInstance().getConfig().set("Warps." + warpName, null);
                        PrisCore.getInstance().saveConfig();
                        String message = WARP_PATTERN.matcher(getString(lang, "Commands.Delwarp.WarpRemoved")).replaceAll(warpName);
                        p.sendMessage(message);

                    } else {
                        p.sendMessage(getString(lang, "Commands.Delwarp.InvalidWarp"));
                    }
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            String warpName = args[0];
            if (PrisCore.getInstance().getConfig().contains("Warps." + warpName)) {
                PrisCore.getInstance().getConfig().set("Warps." + warpName, null);
                PrisCore.getInstance().saveConfig();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Warp has been deleted.");
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid warp.");
            }
        }

        return true;
    }
}
