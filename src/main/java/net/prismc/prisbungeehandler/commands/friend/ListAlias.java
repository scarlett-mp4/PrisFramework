package net.prismc.prisbungeehandler.commands.friend;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ListAlias extends Command {

    public ListAlias(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if (args.length == 0) {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(p, "friend list");
            } else {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(p, "friend list " + args[0]);
            }
        }
    }
}
