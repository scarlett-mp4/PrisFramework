package net.prismc.prisbungeehandler.commands.friend.subcommands;

import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class FriendToggle extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("toggle");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        try {
            if (p.toOfflinePlayer().getSetting(0).equals("1")) {
                p.toOfflinePlayer().setSpecificSetting(0, "0", true);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Requests.ToggledOff"));
            } else {
                p.toOfflinePlayer().setSpecificSetting(0, "1", true);
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Friends.Requests.ToggledOn"));
            }
        } catch (Exception ignored) {
        }
    }
}
