package net.prismc.prishub.commands;

import net.prismc.prishub.PrisHub;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PrisHubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender.hasPermission("prismc.admin")) {
            PrisHub.getInstance().reloadConfig();
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dPrisHub&7] &aHas been reloaded!"));
        }

        return true;
    }
}
