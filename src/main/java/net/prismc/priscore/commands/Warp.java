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

import java.util.Objects;

import static net.prismc.priscore.utils.PatternCollection.PLAYER_PATTERN;
import static net.prismc.priscore.utils.PatternCollection.WARP_PATTERN;

public class Warp extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (p.hasPermission("prismc.warp")) {
                if (args.length == 0) {
                    p.sendMessage(getString(lang, "Commands.Warp.MustIncludeWarp"));
                }

                if (args.length == 1) {
                    String warpName = args[0];
                    if (p.hasPermission("prismc.warp." + warpName)) {
                        try {
                            p.teleport(Objects.requireNonNull(PrisCore.getInstance().getConfig().getLocation("Warps." + warpName)));
                            p.sendMessage(getString(lang, "Commands.Warp.Warped"));
                        } catch (Exception e) {
                            p.sendMessage(getString(lang, "Commands.Warp.InvalidWarp"));
                        }
                    } else {
                        p.sendMessage(getString(lang, "Commands.MissingPermission"));
                    }

                } else if (args.length == 2) {
                    PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[1]));
                    String warpName = args[0];
                    if (PrisCore.getInstance().getConfig().contains("Warps." + warpName)) {
                        if (p.hasPermission("prismc.warp.others")) {
                            try {
                                t.teleport(Objects.requireNonNull(PrisCore.getInstance().getConfig().getLocation("Warps." + warpName)));
                                t.sendMessage(getString(lang, "Commands.Warp.Warped"));
                                String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Warp.WarpOthers")).replaceAll(t.getName());
                                String message2 = WARP_PATTERN.matcher(message).replaceAll(warpName);
                                p.sendMessage(message2);

                            } catch (Exception e) {
                                p.sendMessage(getString(lang, "Commands.PlayerNotOnline"));
                            }
                        } else {
                            p.sendMessage(getString(lang, "Commands.MissingPermission"));
                        }
                    } else {
                        p.sendMessage(getString(lang, "Commands.Warp.InvalidWarp"));
                    }
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            if (args.length == 2) {
                String warpName = args[0];
                PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[1]));

                if (PrisCore.getInstance().getConfig().contains("Warps." + warpName)) {
                    try {
                        t.teleport(Objects.requireNonNull(PrisCore.getInstance().getConfig().getLocation("Warps." + warpName)));
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Warp.Warped"));
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "You have warped the player to " + ChatColor.YELLOW + warpName + ChatColor.LIGHT_PURPLE + "!");
                    } catch (Exception e) {
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "That player is currently not online.");
                    }
                } else {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "That warp does not exist ");
                }
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid arguments.");
            }
        }
        return true;
    }
}
