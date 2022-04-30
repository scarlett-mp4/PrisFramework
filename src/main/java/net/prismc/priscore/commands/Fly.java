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

public class Fly extends UtilApi implements CommandExecutor {

    private void flyMethod(PrisBukkitPlayer p) {
        FileConfiguration lang = p.getLanguageFile();
        if (p.getAllowFlight()) {
            p.setAllowFlight(false);
            p.sendMessage(getString(lang, "Commands.Fly.FlyOFF"));
            return;
        } else if (!(p.getAllowFlight()))
            p.setAllowFlight(true);
        p.sendMessage(getString(lang, "Commands.Fly.FlyON"));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (p.hasPermission("prismc.fly")) {
                if (args.length == 0) {
                    this.flyMethod(p);
                } else if (args.length == 1 && p.hasPermission("prismc.fly.others")) {

                    try {
                        PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                        this.flyMethod(t);
                        String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Fly.FlyOthers")).replaceAll(t.getName());
                        p.sendMessage(message);
                    } catch (Exception e) {
                        p.sendMessage(getString(lang, "Commands.PlayerNotOnline"));
                    }
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else if (args.length == 1) {

            try {
                PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                this.flyMethod(p);
            } catch (Exception e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "That player is currently not online.");
            }
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid arguments.");
        }

        return true;
    }
}

