package net.prismc.prisbungeehandler.commands.party;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyListAlias extends Command {

    public PartyListAlias(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            ProxyServer.getInstance().getPluginManager().dispatchCommand(p, "party list");
        }
    }
}
