package net.prismc.prisbungeehandler.commands.levels;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class Levels extends Command {

    public Levels(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            PrisPlayer p = new PrisApi().wrapPlayer((ProxiedPlayer) commandSender);
            if (p.hasPermission("prismc.admin")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("level")) {
                        if (args.length > 1) {
                            if (args[1].equalsIgnoreCase("add")) {
                                if (args.length > 2) {
                                    p.toOfflinePlayer().setLevel(p.toOfflinePlayer().getLevel() + Integer.parseInt(args[2]), true);
                                    p.sendMessage("&4DEV Levels: &aExecuted.");
                                } else {
                                    p.sendMessage("&4DEV Levels: &cYou must define an integer value.");
                                }
                            } else if (args[1].equalsIgnoreCase("set")) {
                                if (args.length > 2) {
                                    p.toOfflinePlayer().setLevel(Integer.parseInt(args[2]), true);
                                    p.toOfflinePlayer().setProgress(0, true);
                                    p.sendMessage("&4DEV Levels: &aExecuted.");
                                } else {
                                    p.sendMessage("&4DEV Levels: &cYou must define an integer value.");
                                }
                            } else {
                                p.sendMessage("&4DEV Levels: &cYou must set an operator; Try: \"add\" or \"set\"");
                            }
                        } else {
                            p.sendMessage("&4DEV Levels: &cUnknown argument; Try: \"add\" or \"set\"");
                        }
                    } else if (args[0].equalsIgnoreCase("xp")) {
                        if (args.length > 1) {
                            if (args[1].equalsIgnoreCase("add")) {
                                if (args.length > 2) {
                                    p.toOfflinePlayer().addXP(Integer.parseInt(args[2]), true);
                                    p.sendMessage("&4DEV Levels: &aExecuted.");
                                } else {
                                    p.sendMessage("&4DEV Levels: &cYou must define an integer value.");
                                }
                            } else if (args[1].equalsIgnoreCase("set")) {
                                if (args.length > 2) {
                                    p.toOfflinePlayer().setProgress(Integer.parseInt(args[2]), true);
                                    p.sendMessage("&4DEV Levels: &aExecuted.");
                                } else {
                                    p.sendMessage("&4DEV Levels: &cYou must define an integer value.");
                                }
                            } else {
                                p.sendMessage("&4DEV Levels: &cYou must set an operator; Try: \"add\" or \"set\"");
                            }
                        } else {
                            p.sendMessage("&4DEV Levels: &cUnknown argument; Try: \"add\" or \"set\"");
                        }
                    } else {
                        p.sendMessage("&4DEV Levels: &cUnknown argument; Try: \"level\" or \"xp\"");
                    }
                } else {
                    p.sendMessage("&4DEV Levels: &cIncomplete arguments. Try: \"level\" or \"xp\"");
                }
            }
        }
    }
}
