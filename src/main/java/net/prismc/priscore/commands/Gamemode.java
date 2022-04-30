package net.prismc.priscore.commands;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static net.prismc.priscore.utils.PatternCollection.PLAYER_PATTERN;

public class Gamemode extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // If sender is a player...
        if (sender instanceof Player) {
            PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer((Player) sender);
            FileConfiguration lang = p.getLanguageFile();

            if (sender.hasPermission("prismc.gamemode")) {
                if (args.length == 0) {
                    p.sendMessage(getString(lang, "Commands.Gamemode.ChooseAGamemode"));

                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                        p.setGameMode(GameMode.CREATIVE);
                        p.sendMessage(getString(lang, "Commands.Gamemode.Creative"));
                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage(getString(lang, "Commands.Gamemode.Survival"));
                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                        p.setGameMode(GameMode.ADVENTURE);
                        p.sendMessage(getString(lang, "Commands.Gamemode.Adventure"));
                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spec")) {
                        p.setGameMode(GameMode.SPECTATOR);
                        p.sendMessage(getString(lang, "Commands.Gamemode.Spectator"));
                    } else {
                        p.sendMessage(getString(lang, "Commands.Gamemode.UnknownGamemode"));
                    }

                } else if (args.length == 2 && p.hasPermission("prismc.gamemode.others")) {

                    try {
                        PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            t.setGameMode(GameMode.CREATIVE);
                            t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Creative"));
                            String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Gamemode.GamemodeOthers")).replaceAll(t.getName());
                            p.sendMessage(message);
                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            t.setGameMode(GameMode.SURVIVAL);
                            t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Survival"));
                            String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Gamemode.GamemodeOthers")).replaceAll(t.getName());
                            p.sendMessage(message);
                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            t.setGameMode(GameMode.ADVENTURE);
                            t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Adventure"));
                            String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Gamemode.GamemodeOthers")).replaceAll(t.getName());
                            p.sendMessage(message);
                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spec")) {
                            t.setGameMode(GameMode.SPECTATOR);
                            t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Spectator"));
                            String message = PLAYER_PATTERN.matcher(getString(lang, "Commands.Gamemode.GamemodeOthers")).replaceAll(t.getName());
                            p.sendMessage(message);
                        } else {
                            p.sendMessage(getString(lang, "Commands.Gamemode.UnknownGamemode"));
                        }
                    } catch (Exception e) {
                        p.sendMessage(getString(lang, "Commands.PlayerNotOnline"));
                    }

                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                }
            } else {
                p.sendMessage(getString(lang, "Commands.MissingPermission"));
            }

            // If sender is something else...
        } else {
            if (args.length == 2) {

                try {
                    PrisBukkitPlayer t = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));
                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                        t.setGameMode(GameMode.CREATIVE);
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Creative"));
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "You have updated " + ChatColor.YELLOW + args[1] + ChatColor.LIGHT_PURPLE + "'s gamemode.");
                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                        t.setGameMode(GameMode.SURVIVAL);
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Survival"));
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "You have updated " + ChatColor.YELLOW + args[1] + ChatColor.LIGHT_PURPLE + "'s gamemode.");
                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                        t.setGameMode(GameMode.ADVENTURE);
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Advemture"));
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "You have updated " + ChatColor.YELLOW + args[1] + ChatColor.LIGHT_PURPLE + "'s gamemode.");
                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spec")) {
                        t.setGameMode(GameMode.SPECTATOR);
                        t.sendMessage(getString(t.getLanguageFile(), "Commands.Gamemode.Spectator"));
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "You have updated " + ChatColor.YELLOW + args[1] + ChatColor.LIGHT_PURPLE + "'s gamemode.");
                    } else {
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unknown gamemode! Please choose either creative, survival, adventure, or spectator!");
                    }
                } catch (Exception e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "That player is currently not online!");
                }
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Not enough arguments!");
            }

        }

        return true;
    }
}

