package net.prismc.priscore.commands;

import net.md_5.bungee.api.ChatColor;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BukkitDebug extends UtilApi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("prismc.admin")) {

                if (args.length == 0) {

                    try {
                        StringBuilder online = new StringBuilder();
                        for (PrisBukkitPlayer prisPlayer : PrisCore.getInstance().cache.values()) {
                            online.append(ChatColor.GREEN).append(prisPlayer.getUsername()).append(", ");
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPrisBukkitPlayer Cache: &d(") + PrisCore.getInstance().cache.size() + ") " + online.substring(0, online.length() - 2));
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPrisBukkitPlayer Cache: &d(0)"));
                    }

                } else {
                    try {
                        PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(args[0]));

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlayer Information:"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eid: &7" + prisBukkitPlayer.getID()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eusername: &7" + prisBukkitPlayer.getUsername()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&elang: &7" + prisBukkitPlayer.getLanguage()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&elastseen: &7" + prisBukkitPlayer.getLastOnline()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&efirstjoined: &7" + prisBukkitPlayer.getFirstJoin()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eip: &7" + prisBukkitPlayer.getIP()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eplaytime: &7" + prisBukkitPlayer.getPlayTime()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&elevel: &7" + prisBukkitPlayer.getPrisLevel()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eprogress: &7" + prisBukkitPlayer.getProgress()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&esettings: &7" + prisBukkitPlayer.getAllSettings()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eachievements: &7" + prisBukkitPlayer.getCompletedAchievementsCache()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&epartyobject: &7" + prisBukkitPlayer.getParty().toString()));
                        prisBukkitPlayer.fetchFriendList().whenComplete((object, throwable) -> {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&efriendsobject: &7" + object.toString()));
                        });


                    } catch (Exception e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4DEV BukkitDebug: &cPlayer unavailable."));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4DEV BukkitDebug: &f/bukkitdebug <player> dump ; For more info."));
                        try {
                            if (args[1] != null) {
                                if (args[1].equalsIgnoreCase("dump")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6StackTrace: &7" + Arrays.toString(e.getStackTrace())));
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
        return true;
    }
}