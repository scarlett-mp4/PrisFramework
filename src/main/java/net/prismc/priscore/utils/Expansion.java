package net.prismc.priscore.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Expansion extends PlaceholderExpansion {
    private final PrisCore plugin;

    public Expansion(PrisCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "prismc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "PrisMC";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer(player);
        switch (params.toLowerCase()) {
            case "name":
                return p.getName();
            case "lang":
                return p.getLanguage();
            case "lastseen":
                return p.getLastOnline();
            case "firstjoin":
                return p.getFirstJoin();
            case "settings":
                return p.getAllSettings();
            case "level":
                return String.valueOf(p.getPrisLevel());
            case "progress":
                return String.valueOf(p.getProgress());
            case "playtime":
                return String.valueOf(p.getPlayTime());
        }
        return null;
    }
}
