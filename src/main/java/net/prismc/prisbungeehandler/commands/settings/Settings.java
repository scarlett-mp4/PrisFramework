package net.prismc.prisbungeehandler.commands.settings;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.commands.settings.subcommands.Change;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.ArrayList;

public class Settings extends Command {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public Settings(String name, String permission, String... alias) {
        super(name, permission, alias);
        subcommands.add(new Change());
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            if (commandSender.hasPermission("prismc.admin")) {
                PrisApi api = new PrisApi();
                PrisPlayer player = api.wrapPlayer((ProxiedPlayer) commandSender);

                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        for (String s : getSubcommands().get(i).getNames()) {
                            if (args[0].equalsIgnoreCase(s)) {
                                getSubcommands().get(i).perform(player, args);
                                return;
                            }
                        }
                    }
                    player.sendMessage("&4DEV Settings: &cUnknown subcommand.");
                } else {
                    player.sendMessage("&4DEV Settings: &cMissing arguments.");
                }
            }
        }
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

}