package net.prismc.prisbungeehandler.commands.prisdebug;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.util.Arrays;

public class PrisDebug extends Command {

    public PrisDebug(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            if (commandSender.hasPermission("prismc.admin")) {
                PrisApi api = new PrisApi();
                PrisPlayer p = api.wrapPlayer((ProxiedPlayer) commandSender);

                if (args.length == 0) {

                    StringBuilder online = new StringBuilder();
                    for (PrisPlayer prisPlayer : PrisBungeeHandler.getInstance().cache.onlinePlayers.values()) {
                        online.append(ChatColor.GREEN).append(prisPlayer.getUsername()).append(", ");
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lOnline Cache: &d(") + PrisBungeeHandler.getInstance().cache.onlinePlayers.size() + ") " + online.substring(0, online.length() - 2));

                    StringBuilder offline = new StringBuilder();
                    for (OfflinePrisPlayer prisPlayer : PrisBungeeHandler.getInstance().cache.offlinePlayers) {
                        offline.append(ChatColor.GRAY).append(prisPlayer.getUsername()).append(", ");
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lOffline Cache: &d(") + PrisBungeeHandler.getInstance().cache.offlinePlayers.size() + ") " + offline.substring(0, offline.length() - 2));

                    try {
                        StringBuilder partyStr = new StringBuilder();
                        for (PrisParty party : PrisBungeeHandler.getInstance().cache.parties) {
                            partyStr.append(ChatColor.YELLOW).append(party.getLeader().getUsername()).append("'s Party, ");
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lParty Cache: &d(") + PrisBungeeHandler.getInstance().cache.parties.size() + ") " + partyStr.substring(0, partyStr.length() - 2));
                    } catch (Exception ignored) {
                    }
                } else {
                    try {
                        OfflinePrisPlayer offlinePrisPlayer = api.wrapOfflinePlayer(args[0]);

                        p.sendMessage("&6&lPlayer Information:");
                        p.sendMessage("&eid: &7" + offlinePrisPlayer.getID());
                        p.sendMessage("&eusername: &7" + offlinePrisPlayer.getUsername());
                        p.sendMessage("&elang: &7" + offlinePrisPlayer.getLanguage());
                        p.sendMessage("&elastseen: &7" + offlinePrisPlayer.getLastOnline());
                        p.sendMessage("&efirstjoined: &7" + offlinePrisPlayer.getFirstJoin());
                        p.sendMessage("&eip: &7" + offlinePrisPlayer.getIP());
                        p.sendMessage("&eplaytime: &7" + offlinePrisPlayer.getPlayTime());
                        p.sendMessage("&elevel: &7" + offlinePrisPlayer.getLevel());
                        p.sendMessage("&eprogress: &7" + offlinePrisPlayer.getProgress());
                        p.sendMessage("&esettings: &7" + offlinePrisPlayer.getAllSettings());
                        try {
                            PrisPlayer player = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                            p.sendMessage("&esession: &7" + player.getSessionTime());
                        } catch (Exception e) {
                            p.sendMessage("&esession: &cOffline");
                        }
                        try {
                            p.sendMessage("&epartyleader: &7" + offlinePrisPlayer.getParty().getLeader().getUsername());
                        } catch (Exception e) {
                            p.sendMessage("&epartyleader: &cNo Party");
                        }

                    } catch (Exception e) {
                        p.sendMessage("&4DEV PrisDebug: &cPlayer unavailable.");
                        p.sendMessage("&4DEV PrisDebug: &f/prisdebug <player> dump ; For more info.");
                        if (args[1] != null) {
                            if (args[1].equalsIgnoreCase("dump")) {
                                p.sendMessage("&6StackTrace: &7" + Arrays.toString(e.getStackTrace()));
                            }
                        }
                    }
                }
            }
        }
    }
}
