package net.prismc.prisbungeehandler.commands.settings.subcommands;

import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;

public class Change extends SubCommand {

    @Override
    public List<String> getNames() {
        return Collections.singletonList("change");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        try {
            if (args[1] != null && args[2] != null) {
                if (args[3] != null) {
                    try {
                        PrisApi api = new PrisApi();
                        OfflinePrisPlayer offlinePrisPlayer = api.wrapOfflinePlayer(args[3]);
                        offlinePrisPlayer.setSpecificSetting(Integer.parseInt(args[1]), String.valueOf(args[2]), true);
                        p.sendMessage("&4DEV Settings: &aSetting changed for " + offlinePrisPlayer.getUsername() + ".");
                    } catch (Exception e) {
                        p.sendMessage("&4DEV Settings: &cPlayer not found.");
                    }
                    return;
                }
                try {
                    p.toOfflinePlayer().setSpecificSetting(Integer.parseInt(args[1]), String.valueOf(args[2]), true);
                    p.sendMessage("&4DEV Settings: &aSetting changed.");
                } catch (Exception e) {
                    p.sendMessage("&4DEV Settings: &cInvalid arguments.");
                }
            } else {
                p.sendMessage("&4DEV Settings: &cMissing arguments.");
            }
        } catch (Exception e) {
            p.sendMessage("&4DEV Settings: &cMissing arguments.");
        }
    }
}
