package net.prismc.prisbungeehandler.events.bukkit;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class SuffixUpdateEvent extends Event {
    private final PrisPlayer player;
    private final JsonObject object;

    public SuffixUpdateEvent(PrisPlayer player, JsonObject object) {
        this.player = player;
        this.object = object;
    }

    public PrisPlayer getPlayer() {
        return player;
    }

    public String getSuffix() {
        return object.get("suffix").getAsString();
    }
}
