package net.prismc.prisbungeehandler.prisplayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.config.Configuration;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.communication.sql.PrisSQL;
import net.prismc.prisbungeehandler.events.pris.PrisAchievementEvent;
import org.joda.time.LocalDateTime;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static net.prismc.prisbungeehandler.utils.PatternCollection.PLAYER_PATTERN;

public class PrisPlayer {

    private final ProxiedPlayer proxiedPlayer;
    private final HashMap<Integer, Integer> incomingFriendRequests = new HashMap<>();
    private final HashMap<Integer, Integer> incomingPartyInvites = new HashMap<>();
    private final LocalDateTime sessionStartTime = LocalDateTime.now();
    private final List<OfflinePrisPlayer> cachedFriends = new ArrayList<>();
    private final OfflinePrisPlayer offlinePrisPlayer;
    private final List<String> completedAchievements = new ArrayList<>();
    private final int sessionId;
    private boolean temporaryPartyOverride = false;
    private boolean newSession = true;
    private int sessionTime = 0;

    public PrisPlayer(ProxiedPlayer player) {
        this.proxiedPlayer = player;
        PrisApi api = new PrisApi();
        offlinePrisPlayer = api.wrapOfflinePlayer(player.getUniqueId());
        PrisBungeeHandler.getInstance().cache.onlinePlayers.put(this.getUniqueId(), this);

        sessionId = ProxyServer.getInstance().getScheduler().schedule(PrisBungeeHandler.getInstance(), () -> {
            sessionTime = sessionTime + 1;
            if (sessionTime == 3601 || sessionTime == 18001 || sessionTime == 86401 || sessionTime == 601) {
                PrisApi.achievementCheckAndExecute(this);
            }
        }, 0, 1, TimeUnit.SECONDS).getId();

        enableAchievements();
    }


    // PrisMC Methods
    public Configuration getLanguageFile() {
        String lang = offlinePrisPlayer.getLanguage();

        switch (lang) {
            case "english":
                return PrisBungeeHandler.getInstance().engConfig.getConfig();
            case "spanish":
                return PrisBungeeHandler.getInstance().spaConfig.getConfig();
            case "german":
                return PrisBungeeHandler.getInstance().gerConfig.getConfig();
            case "french":
                return PrisBungeeHandler.getInstance().freConfig.getConfig();
        }

        ProxyServer.getInstance().getLogger().severe(proxiedPlayer.getName() + " has an invalid or missing language defined!");
        return PrisBungeeHandler.getInstance().engConfig.getConfig();
    }

    private void enableAchievements() {
        ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
            try {
                completedAchievements.addAll(new PrisSQL().getAchievements(this));
                updateBukkitAchievements();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isNewSession() {
        return newSession;
    }

    public void setNewSession(boolean value) {
        newSession = value;
    }

    public boolean isPartyOverride() {
        return temporaryPartyOverride;
    }

    public OfflinePrisPlayer toOfflinePlayer() {
        return offlinePrisPlayer;
    }

    public void quit() {
        ProxyServer.getInstance().getScheduler().cancel(sessionId);
        PrisBungeeHandler.getInstance().cache.onlinePlayers.remove(this.getUniqueId());
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public void addIncomingFriendRequest(int playerID, int taskID) {
        incomingFriendRequests.put(playerID, taskID);
    }

    public boolean containsIncomingFriendRequest(OfflinePrisPlayer offlinePrisPlayer) {
        return incomingFriendRequests.containsKey(offlinePrisPlayer.getID());
    }

    public int getIncomingFriendRequestTask(OfflinePrisPlayer offlinePrisPlayer) {
        return incomingFriendRequests.get(offlinePrisPlayer.getID());
    }

    public HashMap<Integer, Integer> getIncomingFriendRequests() {
        return incomingFriendRequests;
    }

    public void removeIncomingFriendRequest(OfflinePrisPlayer offlinePrisPlayer) {
        incomingFriendRequests.remove(offlinePrisPlayer.getID());
    }

    public boolean containsIncomingPartyInvite(OfflinePrisPlayer offlinePrisPlayer) {
        return incomingPartyInvites.containsKey(offlinePrisPlayer.getID());
    }

    public void addIncomingPartyInvite(int playerID, int taskID) {
        incomingPartyInvites.put(playerID, taskID);
    }

    public int getIncomingPartyInviteTask(OfflinePrisPlayer offlinePrisPlayer) {
        return incomingPartyInvites.get(offlinePrisPlayer.getID());
    }

    public HashMap<Integer, Integer> getIncomingPartyInvites() {
        return incomingPartyInvites;
    }

    public void removeIncomingPartyInvite(OfflinePrisPlayer offlinePrisPlayer) {
        incomingPartyInvites.remove(offlinePrisPlayer.getID());
    }

    public void addFriendCache(OfflinePrisPlayer player) {
        cachedFriends.add(player);
        PrisApi.achievementCheckAndExecute(this);
    }

    public void removeFriendCache(OfflinePrisPlayer player) {
        cachedFriends.remove(player);
        PrisApi.achievementCheckAndExecute(this);
    }

    public void addCompletedAchievement(String value) {
        completedAchievements.add(value);
        updateBukkitAchievements();
    }

    public void removeCompletedAchievement(String value) {
        completedAchievements.remove(value);
        updateBukkitAchievements();
    }

    public List<String> getCompletedAchievements() {
        return completedAchievements;
    }

    public boolean containsFriendCache(OfflinePrisPlayer player) {
        return cachedFriends.contains(player);
    }

    public boolean hasAchievement(String achievement) {
        if (!this.isOnline())
            return true;

        return new PrisSQL().hasAchievement(this, achievement);
    }

    public void addAchievement(String achievement) {
        ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
            try {
                new PrisSQL().addAchievement(this, achievement);
                this.toOfflinePlayer().addXP(UtilApi.getAchievementHandler(achievement).getXpAward(), true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ProxyServer.getInstance().getPluginManager().callEvent(new PrisAchievementEvent(this, UtilApi.getAchievementHandler(achievement)));
        });
    }

    public List<OfflinePrisPlayer> getFriendCache() {
        return cachedFriends;
    }

    public void alertFriendsOnline() {
        PrisApi api = new PrisApi();
        for (OfflinePrisPlayer offlinePrisPlayer : cachedFriends) {
            try {
                PrisPlayer friend = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                if (friend.toOfflinePlayer().getSetting(1).equals("1")) {
                    String s = PLAYER_PATTERN.matcher(UtilApi.getString(friend.getLanguageFile(), "Friends.Notifications.Online")).replaceAll(proxiedPlayer.getName());
                    friend.sendMessage(s);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void alertFriendsOffline() {
        PrisApi api = new PrisApi();
        List<OfflinePrisPlayer> players = cachedFriends;
        for (OfflinePrisPlayer offlinePrisPlayer : players) {
            try {
                PrisPlayer friend = api.wrapPlayer(ProxyServer.getInstance().getPlayer(offlinePrisPlayer.getUniqueId()));
                if (friend.toOfflinePlayer().getSetting(1).equals("1")) {
                    String s = PLAYER_PATTERN.matcher(UtilApi.getString(friend.getLanguageFile(), "Friends.Notifications.Offline")).replaceAll(proxiedPlayer.getName());
                    friend.sendMessage(s);
                }
            } catch (Exception ignored) {
            }
        }
    }


    // General ProxiedPlayer Methods
    public String getUsername() {
        return proxiedPlayer.getName();
    }

    public UUID getUniqueId() {
        return proxiedPlayer.getUniqueId();
    }

    public boolean isOnline() {
        return proxiedPlayer.isConnected();
    }

    public void sendMessage(String message) {
        proxiedPlayer.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void sendMessage(BaseComponent message) {
        proxiedPlayer.sendMessage(message);
    }

    public void sendMessage(ChatMessageType position, BaseComponent... message) {
        proxiedPlayer.sendMessage(position, message);
    }

    public void sendMessage(ChatMessageType position, BaseComponent message) {
        proxiedPlayer.sendMessage(position, message);
    }

    public void sendMessage(UUID sender, BaseComponent... message) {
        proxiedPlayer.sendMessage(sender, message);
    }

    public void sendMessage(UUID sender, BaseComponent message) {
        proxiedPlayer.sendMessage(sender, message);
    }

    public void sendMessage(BaseComponent[] baseComponents) {
        proxiedPlayer.sendMessage(baseComponents);
    }

    public void connect(ServerInfo target) {
        proxiedPlayer.connect(target);
    }

    public void connect(ServerInfo target, boolean PartyOverride) {
        temporaryPartyOverride = true;
        proxiedPlayer.connect(target);
        ProxyServer.getInstance().getScheduler().schedule(PrisBungeeHandler.getInstance(), () -> {
            temporaryPartyOverride = false;
        }, 1, TimeUnit.SECONDS);
    }

    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
        proxiedPlayer.connect(target, reason);
    }

    public void connect(ServerInfo target, Callback<Boolean> callback) {
        proxiedPlayer.connect(target, callback);
    }

    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        proxiedPlayer.connect(target, callback, reason);
    }

    public void connect(ServerConnectRequest request) {
        proxiedPlayer.connect(request);
    }

    public Server getServer() {
        return proxiedPlayer.getServer();
    }

    public boolean isForgeUser() {
        return proxiedPlayer.isForgeUser();
    }

    public Map<String, String> getModList() {
        return proxiedPlayer.getModList();
    }

    public SocketAddress getSocketAddress() {
        return proxiedPlayer.getSocketAddress();
    }

    public boolean hasPermission(String permission) {
        return proxiedPlayer.hasPermission(permission);
    }

    private void updateBukkitAchievements() {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        object.addProperty("subchannel", "completedAchievements");
        object.addProperty("username", getUsername());
        object.addProperty("uuid", getUniqueId().toString());
        object.addProperty("id", toOfflinePlayer().getID());

        for (String s : completedAchievements) {
            array.add(s);
        }
        object.add("achievements", array);

        try {
            out.writeUTF(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getServer().getInfo().sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
    }
}
