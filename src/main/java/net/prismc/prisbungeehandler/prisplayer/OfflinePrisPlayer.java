package net.prismc.prisbungeehandler.prisplayer;

import com.google.gson.JsonObject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.communication.sql.PrisSQL;
import net.prismc.prisbungeehandler.communication.sql.SQL;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.utils.ChatFilterLevels;
import net.prismc.prisbungeehandler.utils.LevelObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class OfflinePrisPlayer {
    private final SQL sql = new SQL();
    private final PrisSQL prisSQL = new PrisSQL();
    private int id;
    private String username;
    private UUID uuid;
    private String lang;
    private String lastSeen;
    private String firstJoined;
    private String ip;
    private int playTime;
    private int level;
    private int progress;
    private String settings;
    private int rubies;
    private int priscoins;
    private int karma;
    private UUID replyUser = null;
    private PrisParty party = null;

    public OfflinePrisPlayer(String name) {
        try {
            assignVariables(prisSQL.getAllData(name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public OfflinePrisPlayer(UUID uuid) {
        try {
            assignVariables(prisSQL.getAllData(uuid));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public OfflinePrisPlayer(int id) {
        try {
            assignVariables(prisSQL.getAllData(id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void assignVariables(HashMap<String, String> map) {
        this.id = Integer.parseInt(map.get("id"));
        this.uuid = UUID.fromString(map.get("uuid"));
        this.username = map.get("username");
        this.lang = map.get("lang");
        this.lastSeen = map.get("lastseen");
        this.firstJoined = map.get("firstjoined");
        this.ip = map.get("ip");
        this.playTime = Integer.parseInt(map.get("playtime"));
        this.level = Integer.parseInt(map.get("level"));
        this.progress = Integer.parseInt(map.get("progress"));
        this.settings = map.get("settings");
        this.priscoins = Integer.parseInt(map.get("priscoins"));
        this.rubies = Integer.parseInt(map.get("rubies"));
        this.karma = Integer.parseInt(map.get("karma"));
        if (uuid != null) {
            PrisBungeeHandler.getInstance().cache.offlinePlayers.add(this);
        }
    }


    // -----| Getters |----------------------------------------------------------------------------------------------------------------

    public UUID getReplyUser() {
        return replyUser;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public int getID() {
        return id;
    }

    public String getLanguage() {
        return lang;
    }

    public String getFirstJoin() {
        return firstJoined;
    }

    public String getIP() {
        return ip;
    }

    public int getPriscoins() {
        return priscoins;
    }

    public int getKarma() {
        return karma;
    }

    public int getPlayTime() {
        return playTime;
    }

    public int getLevel() {
        return level;
    }

    public int getProgress() {
        return progress;
    }

    public String getAllSettings() {
        return settings;
    }

    public String getLastOnline() {
        return lastSeen;
    }

    public int getRubies() {
        return rubies;
    }

    public String getSetting(int settingID) {

        switch (settingID) {
            case 0: // Friend Requests
                return String.valueOf(settings.charAt(0));
            case 1: // Friend Notifications
                return String.valueOf(settings.charAt(1));
            case 2: // MSG Toggle
                return String.valueOf(settings.charAt(2));
            case 3: // Chat Filter
                return String.valueOf(settings.charAt(3));
            case 4: // Party Invites
                return String.valueOf(settings.charAt(4));
        }
        return null;
    }

    public ChatFilterLevels getFilterLevel() {
        switch (String.valueOf(settings.charAt(3))) {
            case "2":
                return ChatFilterLevels.HIGH;
            case "1":
                return ChatFilterLevels.MEDIUM;
            case "0":
                return ChatFilterLevels.LOW;
        }
        return null;
    }

    public String getRankColor() {
        try {
            LuckPerms api = LuckPermsProvider.get();
            String s = api.getGroupManager().getGroup(api.getUserManager().getUser(uuid).getPrimaryGroup()).getDisplayName();
            if (s.equals("null")) {
                return ChatColor.translateAlternateColorCodes('&', "&7");
            }
            return ChatColor.translateAlternateColorCodes('&', s);
        } catch (Exception ignored) {
        }
        return ChatColor.translateAlternateColorCodes('&', "&7");
    }

    public String getRank() {
        try {
            LuckPerms api = LuckPermsProvider.get();
            String s = api.getUserManager().getUser(uuid).getPrimaryGroup().toLowerCase();
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } catch (Exception ignored) {
        }
        return "Default";
    }

    public int getMaxPartyPlayers() {
        try {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
            for (int i = 200; i >= 1; i--) {
                if (player.hasPermission("prismc.party." + i)) {
                    return i;
                }
            }
        } catch (Exception ignored) {
        }
        return 10;
    }


    // -----| Setters |----------------------------------------------------------------------------------------------------------------

    public void setReplyUser(UUID replyName, boolean updateBukkit) {
        this.replyUser = replyName;

        if (updateBukkit) {
            update();
        }
    }

    public void setRubies(int value, boolean updateBukkit) {
        rubies = value;

        if (updateBukkit) {
            update();
        }
    }

    public void setLevel(int value, boolean updateBukkit) {
        level = value;
        checkProgress();
        if (updateBukkit) {
            update();
        }
    }

    public void setUsername(String value, boolean updateBukkit) {
        username = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setIP(String value, boolean updateBukkit) {
        ip = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setProgress(int value, boolean updateBukkit) {
        progress = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setPlayTime(int value, boolean updateBukkit) {
        playTime = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setPriscoins(int value, boolean updateBukkit) {
        priscoins = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setKarma(int value, boolean updateBukkit) {
        karma = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setLang(String value, boolean updateBukkit) {
        lang = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setLastSeen(String value, boolean updateBukkit) {
        lastSeen = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setSettings(String value, boolean updateBukkit) {
        settings = value;
        if (updateBukkit) {
            update();
        }
    }

    public void setSpecificSetting(int identifier, String value, boolean updateBukkit) {
        StringBuilder builder = new StringBuilder(settings);
        builder.setCharAt(identifier, value.charAt(0));
        settings = builder.toString();
        if (updateBukkit) {
            update();
        }
    }


    // -----| Misc |----------------------------------------------------------------------------------------------------------------

    public void addXP(int value, boolean updateBukkit) {
        progress = progress + value;
        checkProgress();
        if (updateBukkit) {
            update();
        }
    }

    public void checkProgress() {
        LevelObject nextLevel;

        try {
            nextLevel = UtilApi.getLevelObject(this.level + 1);


            if (this.progress >= nextLevel.getProgressNeeded()) {
                setProgress(this.progress - nextLevel.getProgressNeeded(), true);
                setLevel(this.level + 1, true);
            }
        } catch (Exception ignored) {
        }
    }

    public void addFriend(OfflinePrisPlayer offlinePrisPlayer) {
        PrisApi api = new PrisApi();

        try {
            sql.insertData("one, two", "'" + this.getID() + "', '" + offlinePrisPlayer.getID() + "'", "pris_friends");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            PrisPlayer player = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(uuid));
            player.addFriendCache(offlinePrisPlayer);
        } catch (Exception ignore) {
        }

        try {
            PrisPlayer player = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(offlinePrisPlayer.getUniqueId()));
            player.addFriendCache(this);
        } catch (Exception ignore) {
        }
    }

    public void removeFriend(OfflinePrisPlayer offlinePrisPlayer) {
        PrisApi api = new PrisApi();
        try {
            sql.deleteData(new String[]{"one='" + this.getID() + "'", "two='" + offlinePrisPlayer.getID() + "'" + " OR "
                    + "one='" + offlinePrisPlayer.getID() + "'", "two='" + this.getID() + "'" + " LIMIT 1"}, "pris_friends");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            PrisPlayer player = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(uuid));
            player.removeFriendCache(offlinePrisPlayer);
        } catch (Exception ignore) {
        }

        try {
            PrisPlayer player = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(offlinePrisPlayer.getUniqueId()));
            player.removeFriendCache(this);
        } catch (Exception ignore) {
        }
    }

    public void addParty(PrisParty party) {
        this.party = party;
        PrisApi.achievementCheckAndExecute(this);
    }

    public void leaveParty() {
        this.party = null;

        try {
            PrisPlayer player = new PrisApi().wrapPlayer(ProxyServer.getInstance().getPlayer(uuid));
            JsonObject partyObject = new JsonObject();
            JsonObject toSend = new JsonObject();
            partyObject.addProperty("leader", "null");

            toSend.addProperty("uuid", uuid.toString());
            toSend.addProperty("subchannel", "partyupdate");
            toSend.add("party", partyObject);
            send(toSend, player);
        } catch (Exception ignored) {
        }
    }

    public boolean inParty() {
        return party != null;
    }

    public PrisParty getParty() {
        return party;
    }

    public boolean isOnline() {
        try {
            if (ProxyServer.getInstance().getPlayer(uuid).isConnected())
                return true;
        } catch (Exception ignored) {
        }
        return false;
    }


    // -----| Communication |----------------------------------------------------------------------------------------------------------------

    private void update() {
        try {
            ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
                PrisPlayer player = new PrisApi().wrapPlayer(ProxyServer.getInstance().getPlayer(uuid));
                JsonObject toSend = new JsonObject();
                toSend.addProperty("subchannel", "generalupdate");
                toSend.addProperty("start", "proxy");
                toSend.addProperty("id", id);
                toSend.addProperty("uuid", uuid.toString());
                toSend.addProperty("username", username);
                toSend.addProperty("lang", lang);
                toSend.addProperty("lastseen", lastSeen);
                toSend.addProperty("firstjoined", firstJoined);
                toSend.addProperty("ip", ip);
                toSend.addProperty("playtime", playTime);
                toSend.addProperty("level", level);
                toSend.addProperty("progress", progress);
                toSend.addProperty("settings", settings);
                toSend.addProperty("priscoins", priscoins);
                toSend.addProperty("rubies", rubies);
                toSend.addProperty("karma", karma);
                if (replyUser == null) {
                    toSend.addProperty("replyUser", "null");
                } else {
                    toSend.addProperty("replyUser", replyUser.toString());
                }
                send(toSend, player);
            });
        } catch (Exception ignored) {
        }
    }

    private void send(JsonObject toSend, PrisPlayer player) {
        if (player.getServer().getInfo() == null)
            return;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(toSend.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.getServer().getInfo().sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
    }
}
