package net.prismc.prisbungeehandler.commands.language;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class Language extends Command {

    public Language(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            if (commandSender.hasPermission("prismc.admin")) {
                PrisApi api = new PrisApi();
                PrisPlayer p = api.wrapPlayer((ProxiedPlayer) commandSender);
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "english":
                            p.toOfflinePlayer().setLang("english", true);
                            p.sendMessage("&4DEV Language: &aLanguage changed to English.");
                            return;
                        case "spanish":
                            p.toOfflinePlayer().setLang("spanish", true);
                            p.sendMessage("&4DEV Language: &aLanguage changed to Spanish.");
                            return;
                        case "french":
                            p.toOfflinePlayer().setLang("french", true);
                            p.sendMessage("&4DEV Language: &aLanguage changed to French.");
                            return;
                        case "german":
                            p.toOfflinePlayer().setLang("german", true);
                            p.sendMessage("&4DEV Language: &aLanguage changed to German.");
                            return;
                        default:
                            p.toOfflinePlayer().setLang("english", true);
                            p.sendMessage("&4DEV Language: &6Unknown language. Defaulting to English...");
                    }
                } else {
                    p.sendMessage("&4DEV Language: &cUnknown language.");
                }
            }
        }
    }
}

