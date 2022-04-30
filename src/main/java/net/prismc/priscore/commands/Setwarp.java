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

public class Setwarp extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (p.hasPermission("prismc.setwarp")) {
                if (args.length == 0) {
                    p.sendMessage(getString(lang, "Commands.InvalidArgs"));

                } else if (args.length == 1) {
                    String warpName = args[0];
                    if (PrisCore.getInstance().getConfig().contains("Warps." + warpName)) {
                        p.sendMessage(getString(lang, "Commands.SetWarp.WarpAlreadyExists"));
                    } else {
                        PrisCore.getInstance().getConfig().set("Warps." + warpName, p.getLocation());
                        PrisCore.getInstance().saveConfig();
                        String message = WARP_PATTERN.matcher(getString(lang, "Commands.SetWarp.WarpSet")).replaceAll(warpName);
                        p.sendMessage(message);
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
