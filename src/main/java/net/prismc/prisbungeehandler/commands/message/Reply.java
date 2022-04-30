package net.prismc.prisbungeehandler.commands.message;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class Reply extends Command {

    public Reply(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
                PrisApi api = new PrisApi();
                PrisPlayer p = api.wrapPlayer((ProxiedPlayer) sender);

                try {
                    if (!args[0].isEmpty()) {

                        try {
                            OfflinePrisPlayer offlinePrisPlayer = api.wrapOfflinePlayer(p.toOfflinePlayer().getReplyUser());
                            if (offlinePrisPlayer.isOnline()) {
                                StringBuilder message = new StringBuilder();
                                for (String s : args) {
                                    message.append(s).append(" ");
                                }

                                ProxyServer.getInstance().getPluginManager().dispatchCommand(sender, "message " + offlinePrisPlayer.getUsername() + " " + message);
                            } else {
                                p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Reply.PlayerIsOffline")));
                            }

                        } catch (Exception e) {
                            p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Reply.NoOneToReplyTo")));
                        }

                    } else {
                        p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Reply.ImproperUsage")));
                    }
                } catch (Exception e) {
                    p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Reply.ImproperUsage")));
                }
            });
        }
    }
}