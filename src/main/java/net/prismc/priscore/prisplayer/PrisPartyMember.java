package net.prismc.priscore.prisplayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PrisPartyMember {

    private final String name;
    private final UUID uuid;
    private final int id;
    private final String role;
    private final OfflinePlayer offlinePlayer;
    private final boolean online;

    public PrisPartyMember(String name, UUID uuid, int id, String role, boolean online) {
        this.name = name;
        this.uuid = uuid;
        this.id = id;
        this.role = role;
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.online = online;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }
}
