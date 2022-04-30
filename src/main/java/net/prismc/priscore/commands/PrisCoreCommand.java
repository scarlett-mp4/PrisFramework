package net.prismc.priscore.commands;

import net.prismc.priscore.PrisCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PrisCoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender.hasPermission("prismc.admin")) {
            PrisCore.getInstance().reloadConfig();
            PrisCore.getInstance().engConfiguration.reloadConfig();
            PrisCore.getInstance().spaConfiguration.reloadConfig();
            PrisCore.getInstance().freConfiguration.reloadConfig();
            PrisCore.getInstance().gerConfiguration.reloadConfig();
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dPrisCore&7] &aHas been reloaded!"));
        }

        return true;
    }
}
