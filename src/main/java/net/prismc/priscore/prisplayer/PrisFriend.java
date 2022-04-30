package net.prismc.priscore.prisplayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PrisFriend {

    private final String name;
    private final String firstJoined;
    private final String lastSeen;
    private final String server;
    private final UUID uuid;
    private final OfflinePlayer offlinePlayer;

    public PrisFriend(String name, String firstJoined, String lastSeen, String server, UUID uuid) {
        this.name = name;
        this.firstJoined = firstJoined;
        this.lastSeen = lastSeen;
        this.server = server;
        this.uuid = uuid;
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
    }

    public String getName() {
        return name;
    }

    public String getFirstJoined() {
        return firstJoined;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getServer() {
        return server;
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public boolean isOnline() {
        return !server.equalsIgnoreCase("OFFLINE");
    }
}
