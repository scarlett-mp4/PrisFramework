package net.prismc.priscore.commands;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static net.prismc.priscore.utils.PatternCollection.PLAYER_PATTERN;

public class Feed extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (p.hasPermission("prismc.feed")) {
                if (args.length == 0) {
                    p.setFoodLevel(20);
                    p.sendMessage(getString(lang, "Commands.Feed.Fed"));

                } else if (args.length == 1 && p.hasPermission("prismc.feed.others")) {
                    try {
                        PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                        t.setFoodLevel(20);
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Feed.Fed"));
                        String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Feed.FedOther")).replaceAll(t.getName());
                        p.sendMessage(message);
                    } catch (Exception e) {
                        p.sendMessage(getString(lang, "Commands.PlayerNotOnline"));
                    }

                } else {
                    p.sendMessage(getString(lang, "Commands.InvalidArgs"));
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            if (args.length == 1) {
                try {
                    PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                    p.setFoodLevel(20);
                    p.sendMessage(getString(p.getLanguageFile(), "Commands.Feed.Fed"));
                    System.out.println("You have replenished player's hunger.");
                } catch (Exception var8) {
                    System.out.println("That player is currently not online.");
                }
            } else {
                System.out.println("Invalid arguments.");
            }
        }

        return true;
    }
}
