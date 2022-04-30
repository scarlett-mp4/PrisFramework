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

public class God extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (p.hasPermission("prismc.god")) {
                if (args.length == 0) {
                    if (p.getPlayer().isInvulnerable()) {
                        p.getPlayer().setInvulnerable(false);
                        p.sendMessage(getString(lang, "Commands.God.Off"));
                    } else {
                        p.getPlayer().setInvulnerable(true);
                        p.sendMessage(getString(lang, "Commands.God.On"));
                    }
                } else {
                    try {
                        PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));

                        if (t.getPlayer().isInvulnerable()) {
                            t.getPlayer().setInvulnerable(false);
                            t.sendMessage(getString(t.getLanguageFile(), "Commands.God.Off"));
                            String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.God.OffOthers")).replaceAll(t.getName());
                            p.sendMessage(message);
                        } else {
                            t.getPlayer().setInvulnerable(true);
                            t.sendMessage(getString(t.getLanguageFile(), "Commands.God.On"));
                            String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.God.OnOthers")).replaceAll(t.getName());
                            p.sendMessage(message);
                        }
                    } catch (Exception e) {
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "That player is currently not online.");
                    }
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            try {
                PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                if (t.getPlayer().isInvulnerable()) {
                    t.getPlayer().setInvulnerable(false);
                    t.sendMessage(getString(t.getLanguageFile(), "Commands.God.Off"));
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + args[0] + ChatColor.LIGHT_PURPLE + " is no longer invulnerable.");
                } else {
                    t.getPlayer().setInvulnerable(true);
                    t.sendMessage(getString(t.getLanguageFile(), "Commands.God.On"));
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + args[0] + ChatColor.LIGHT_PURPLE + " is now invulnerable.");
                }
            } catch (Exception e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "That player is currently not online.");
            }
        }

        return true;
    }
}
