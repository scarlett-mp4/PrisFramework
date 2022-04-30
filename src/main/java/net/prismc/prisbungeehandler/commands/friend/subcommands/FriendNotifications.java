package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Arrays;
import java.util.List;

public class FriendNotifications extends SubCommand {

    @Override
    public List<String> getNames() {
        return Arrays.asList("notifications", "notif", "notifs");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        try {
            if (p.toOfflinePlayer().getSetting(1).equals("1")) {
                p.toOfflinePlayer().setSpecificSetting(1, "0", true);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Notifications.ToggledOff"));
            } else {
                p.toOfflinePlayer().setSpecificSetting(1, "1", true);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Notifications.ToggledOn"));
            }
        } catch (Exception ignored) {
        }
    }
}
