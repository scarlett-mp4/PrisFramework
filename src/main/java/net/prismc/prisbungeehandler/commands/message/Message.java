package net.prismc.prisbungeehandler.commands.message;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import static net.prismc.prisbungeehandler.utils.PatternCollection.MESSAGE_PATTERN;
import static net.prismc.prisbungeehandler.utils.PatternCollection.PLAYER_PATTERN;

public class Message extends Command {

    public Message(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            PrisApi api = new PrisApi();
            PrisPlayer p = api.wrapPlayer((ProxiedPlayer) commandSender);

            try {
                if (!args[0].isEmpty()) {
                    if (args[1].isEmpty()) {
                        p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Message.ImproperUsage"));
                        return;
                    }
                    try {
                        PrisPlayer receiver = api.wrapPlayer(ProxyServer.getInstance().getPlayer(args[0]));
                        if (receiver.toOfflinePlayer().getSetting(2).equals("0")) {
                            if (!p.hasPermission("prismc.admin")) {
                                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Message.CantMessagePlayer"));
                                return;
                            }
                        }
                        if (p.toOfflinePlayer().getID() == receiver.toOfflinePlayer().getID()) {
                            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Message.CantMessageYourself"));
                            return;
                        }
                        if (receiver.toOfflinePlayer().getSetting(2).equals("1")) {
                            if (!p.getFriendCache().contains(receiver.toOfflinePlayer())) {
                                if (!p.hasPermission("prismc.admin")) {
                                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Message.CantMessagePlayer"));
                                    return;
                                }
                            }
                        }

                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i <= args.length - 1; i++) {
                            String s = args[i];
                            message.append(s).append(" ");
                        }

                        String toMessage = UtilApi.parseSwears(message.toString(), p.toOfflinePlayer().getFilterLevel());
                        String fromMessage = UtilApi.parseSwears(message.toString(), receiver.toOfflinePlayer().getFilterLevel());
                        String fromFormat = PLAYER_PATTERN.matcher(UtilApi.getString(receiver.getLanguageFile(), "Message.From")).replaceAll(p.getUsername());
                        String toFormat = PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Message.To")).replaceAll(receiver.getUsername());
                        fromFormat = MESSAGE_PATTERN.matcher(fromFormat).replaceAll(fromMessage);
                        toFormat = MESSAGE_PATTERN.matcher(toFormat).replaceAll(toMessage);
                        p.sendMessage(toFormat);
                        receiver.sendMessage(fromFormat);
                        p.toOfflinePlayer().setReplyUser(receiver.getUniqueId(), true);
                        receiver.toOfflinePlayer().setReplyUser(p.getUniqueId(), true);

                    } catch (Exception e) {
                        p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Message.NotOnline")));
                    }

                } else {
                    p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Message.ImproperUsage")));
                }
            } catch (Exception e) {
                p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Message.ImproperUsage")));
            }
        }
    }
}