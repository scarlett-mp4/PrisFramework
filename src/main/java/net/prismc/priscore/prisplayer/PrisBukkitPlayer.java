package net.prismc.priscore.prisplayer;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.AchievementParseApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.events.PrisLevelUpEvent;
import net.prismc.priscore.events.PrisProgressEvent;
import net.prismc.priscore.utils.ChatFilterLevels;
import net.prismc.priscore.utils.LevelObject;
import net.prismc.priscore.utils.NumInfo;
import net.prismc.priscore.utils.PatternCollection;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PrisBukkitPlayer {
    private final int id;
    private final String username;
    private final UUID uuid;
    private final String lastSeen;
    private final String firstJoined;
    private final String ip;
    private final int playTime;
    private final List<String> completedAchievements = new ArrayList<>();
    private JsonObject bukkitParty;
    private Player bukkitPlayer;
    private String lang;
    private int level;
    private int progress;
    private String settings;
    private int rubies;
    private int priscoins;
    private int karma;
    private CompletableFuture<JsonObject> future;
    private JsonObject tempFriendObject;
    private int actionBarTaskId = 0;

    public PrisBukkitPlayer(JsonObject object) {
        this.id = object.get("id").getAsInt();
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.username = object.get("username").getAsString();
        this.lang = object.get("lang").getAsString();
        this.lastSeen = object.get("lastseen").getAsString();
        this.firstJoined = object.get("firstjoined").getAsString();
        this.ip = object.get("ip").getAsString();
        this.playTime = object.get("playtime").getAsInt();
        this.level = object.get("level").getAsInt();
        this.progress = object.get("progress").getAsInt();
        this.settings = object.get("settings").getAsString();
        this.priscoins = object.get("priscoins").getAsInt();
        this.rubies = object.get("rubies").getAsInt();
        this.karma = object.get("karma").getAsInt();
        this.bukkitParty = object.get("party").getAsJsonObject();

        try {
            this.bukkitPlayer = Bukkit.getPlayer(uuid);
            PrisCore.getInstance().cache.put(uuid, this);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "New PrisBukkitPlayer.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to create PrisBukkitPlayer!");
        }
    }

    public CompletableFuture<JsonObject> getFuture() {
        return future;
    }

    public void quit() {
        PrisCore.getInstance().cache.remove(uuid);
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

    public int getPrisLevel() {
        return level;
    }

    public String getFirstJoin() {
        return firstJoined;
    }

    public String getIP() {
        return ip;
    }

    public int getPlayTime() {
        return playTime;
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

    public JsonObject getParty() {
        return bukkitParty;
    }

    public void setParty(JsonObject value) {
        bukkitParty = value;
    }

    public JsonObject getTempFriendObject() {
        return tempFriendObject;
    }

    public void setTempFriendObject(JsonObject value) {
        tempFriendObject = value;
    }

    public String getSetting(int settingID) {
        return String.valueOf(settings.charAt(settingID));
    }

    public void achievementCheck() {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "achievementCheck");
        send(toSend);
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

    public FileConfiguration getLanguageFile() {
        switch (lang) {
            case "english":
                return PrisCore.getInstance().engConfiguration.getConfig();
            case "spanish":
                return PrisCore.getInstance().spaConfiguration.getConfig();
            case "french":
                return PrisCore.getInstance().freConfiguration.getConfig();
            case "german":
                return PrisCore.getInstance().gerConfiguration.getConfig();
        }
        return PrisCore.getInstance().engConfiguration.getConfig();
    }

    public void setPrisLevel(int value, boolean updateBungee) {
        level = value;
        Bukkit.getPluginManager().callEvent(new PrisLevelUpEvent(this, UtilApi.getLevelObject(value - 1), UtilApi.getLevelObject(value)));
        if (updateBungee) {
            update();
        }
    }

    public void getFuture(CompletableFuture<JsonObject> value) {
        future = value;
    }

    public void setProgress(int value, boolean updateBungee) {
        progress = value;
        Bukkit.getScheduler().runTaskLater(PrisCore.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(new PrisProgressEvent(this, UtilApi.getLevelObject(value)));
        }, 5L);
        if (updateBungee) {
            update();
        }
    }

    public void addXP(int value, boolean updateBungee) {
        progress = value + progress;
        Bukkit.getScheduler().runTaskLater(PrisCore.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(new PrisProgressEvent(this, UtilApi.getLevelObject(value)));
        }, 5L);
        if (updateBungee) {
            update();
        }
    }

    public void setLang(String value, boolean updateBungee) {
        lang = value;
        if (updateBungee) {
            update();
        }
    }

    public void setSettings(String value, boolean updateBungee) {
        settings = value;
        if (updateBungee) {
            update();
        }
    }

    public void setPriscoins(int value, boolean updateBungee) {
        priscoins = value;
        if (updateBungee) {
            update();
        }
    }

    public void setRubies(int value, boolean updateBungee) {
        rubies = value;
        if (updateBungee) {
            update();
        }
    }

    public void setKarma(int value, boolean updateBungee) {
        karma = value;
        if (updateBungee) {
            update();
        }
    }

    public void setPartyStatus() {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "partystatus");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        send(toSend);
    }

    public void setSpecificSetting(int identifier, String value, boolean updateBungee) {
        StringBuilder builder = new StringBuilder(settings);
        builder.setCharAt(identifier, value.charAt(0));
        settings = builder.toString();
        if (updateBungee) {
            update();
        }
    }

    public void joinParty(String partyName) {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "joinparty");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        toSend.addProperty("partyname", partyName);
        send(toSend);
    }

    public void createParty(boolean open) {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "createparty");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        toSend.addProperty("open", open);
        send(toSend);
    }

    public void inviteToParty(String name) {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "inviteparty");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        toSend.addProperty("user", name);
        send(toSend);
    }

    public void leaveParty() {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "leaveparty");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        send(toSend);
    }

    public void addFriend(String friendName) {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "addfriend");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        toSend.addProperty("friendname", friendName);
        send(toSend);
    }

    public void removeFriend(String friendName) {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "removefriend");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        toSend.addProperty("friendname", friendName);
        send(toSend);
    }

    public CompletableFuture<JsonObject> fetchFriendList() {
        future = new CompletableFuture<>();
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "getfriendlist");
        toSend.addProperty("id", id);
        toSend.addProperty("username", username);
        toSend.addProperty("uuid", uuid.toString());
        send(toSend);
        return future;
    }

    private void update() {
        JsonObject toSend = new JsonObject();
        toSend.addProperty("subchannel", "bukkitplayerupdate");
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
        send(toSend);
    }

    private void send(JsonObject toSend) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(toSend.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        getPlayer().sendPluginMessage(PrisCore.getInstance(), PrisCore.BUNGEE_CHANNEL, stream.toByteArray());
    }

    public Player getPlayer() {
        try {
            bukkitPlayer.getInventory();
        } catch (Exception e) {
            // I honestly don't know why this is being such a pain in the ass, but I'm too lazy to properly fix it so...
            try {
                bukkitPlayer = Bukkit.getPlayer(uuid);
            } catch (Exception exception) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SEVERE ERROR: Could not obtain BukkitPlayer via UUID! Trying username...");
                bukkitPlayer = Bukkit.getPlayer(username);
            }
        } finally {
            return bukkitPlayer;
        }
    }

    public void createLevelBar() {
        if (actionBarTaskId > 0) {
            Bukkit.getScheduler().cancelTask(actionBarTaskId);
        }

        String message = getActionBarMessage();
        actionBarTaskId = new BukkitRunnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                }
            }
        }.runTaskTimer(PrisCore.getInstance(), 0, 10).getTaskId();
    }

    public boolean isLevelBarDisplayed() {
        return actionBarTaskId > 0;
    }

    public void destroyLevelBar() {
        if (actionBarTaskId > 0) {
            Bukkit.getScheduler().cancelTask(actionBarTaskId);
            actionBarTaskId = 0;
        }
    }

    private String getActionBarMessage() {
        String progressBar = "\uF12C";
        LevelObject levelObject = UtilApi.getLevelObject(getPrisLevel() + 1);
        int ch = 61741;
        for (double sub = 1.0; sub >= 0; sub = sub - 0.01) {
            int test;
            try {
                test = (int) ((double) levelObject.getProgressNeeded() * sub);
            } catch (Exception e) {
                break;
            }
            if (getProgress() > test) {
                ch = (int) ((double) ch + (sub * 100.0));
                progressBar = String.valueOf((char) ch);
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        char[] array = String.valueOf(getPrisLevel()).toCharArray();
        for (char character : array) {
            for (NumInfo info : NumInfo.values()) {
                if (info.getNumber() == Integer.parseInt(String.valueOf(character))) {
                    builder.append(info.getUnicode());
                }
            }
        }

        String message;
        if (builder.length() == 1) {
            message = UtilApi.getString(PrisCore.getInstance().getConfig(), "ActionBar.1Digit");
        } else if (builder.length() == 2) {
            message = UtilApi.getString(PrisCore.getInstance().getConfig(), "ActionBar.2Digit");
        } else {
            message = UtilApi.getString(PrisCore.getInstance().getConfig(), "ActionBar.3Digit");
        }
        message = PatternCollection.PROGRESS_PATTERN.matcher(message).replaceAll(progressBar);
        return message = PatternCollection.LEVEL_PATTERN.matcher(message).replaceAll(builder.toString());
    }

    public void addCompletedAchievementsCache(String value) {
        completedAchievements.add(value);
    }

    public List<String> getCompletedAchievementsCache() {
        return completedAchievements;
    }

    public void clearCompletedAchievementsCache() {
        completedAchievements.clear();
    }

    public List<AchievementParseApi> getCompletedAchievements() {
        List<AchievementParseApi> list = new ArrayList<>();
        for (String s : completedAchievements) {
            list.add(PrisCore.getInstance().achievementHandler.getAchievement(s));
        }
        return list;
    }

    // -----| Bukkit Methods |----------------------------------------------------------------------------------------------------------------

    public boolean isBanned() {
        return getPlayer().isBanned();
    }

    public boolean isOnline() {
        return getPlayer().isOnline();
    }

    public long getFirstPlayed() {
        return getPlayer().getFirstPlayed();
    }

    public boolean hasPlayedBefore() {
        return getPlayer().hasPlayedBefore();
    }

    public String getDisplayName() {
        return getPlayer().getDisplayName();
    }

    public void setDisplayName(String s) {
        getPlayer().setDisplayName(s);
    }

    public String getPlayerListName() {
        return getPlayer().getPlayerListName();
    }

    public void setPlayerListName(String s) {
        getPlayer().setPlayerListName(s);
    }

    public String getPlayerListHeader() {
        return getPlayer().getPlayerListHeader();
    }

    public void setPlayerListHeader(String s) {
        getPlayer().setPlayerListHeader(s);
    }

    public String getPlayerListFooter() {
        return getPlayer().getPlayerListFooter();
    }

    public void setPlayerListFooter(String s) {
        getPlayer().setPlayerListFooter(s);
    }

    public void setPlayerListHeaderFooter(String s, String s1) {
        getPlayer().setPlayerListHeaderFooter(s, s1);
    }

    public Location getCompassTarget() {
        return getPlayer().getCompassTarget();
    }

    public void setCompassTarget(Location location) {
        getPlayer().setCompassTarget(location);
    }

    public InetSocketAddress getAddress() {
        return getPlayer().getAddress();
    }

    public void sendRawMessage(String s) {
        getPlayer().sendRawMessage(s);
    }

    public void kickPlayer(String s) {
        getPlayer().kickPlayer(s);
    }

    public void chat(String s) {
        getPlayer().chat(s);
    }

    public boolean performCommand(String s) {
        return getPlayer().performCommand(s);
    }

    public boolean isSneaking() {
        return getPlayer().isSneaking();
    }

    public void setSneaking(boolean b) {
        getPlayer().setSneaking(b);
    }

    public boolean isSprinting() {
        return getPlayer().isSprinting();
    }

    public void setSprinting(boolean b) {
        getPlayer().setSprinting(b);
    }

    public void saveData() {
        getPlayer().saveData();
    }

    public void loadData() {
        getPlayer().loadData();
    }

    public boolean isSleepingIgnored() {
        return getPlayer().isSleepingIgnored();
    }

    public void setSleepingIgnored(boolean b) {
        getPlayer().setSleepingIgnored(b);
    }

    public Location getBedSpawnLocation() {
        return getPlayer().getBedSpawnLocation();
    }

    public void setBedSpawnLocation(Location location) {
        getPlayer().setBedSpawnLocation(location);
    }

    public void setBedSpawnLocation(Location location, boolean b) {
        getPlayer().setBedSpawnLocation(location, b);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        getPlayer().playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        getPlayer().playSound(location, s, v, v1);
    }

    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
        getPlayer().playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
        getPlayer().playSound(location, s, soundCategory, v, v1);
    }

    public void stopSound(Sound sound) {
        getPlayer().stopSound(sound);
    }

    public void stopSound(String s) {
        getPlayer().stopSound(s);
    }

    public void stopSound(Sound sound, SoundCategory soundCategory) {
        getPlayer().stopSound(sound, soundCategory);
    }


    public void stopSound(String s, SoundCategory soundCategory) {
        getPlayer().stopSound(s, soundCategory);
    }

    public float getExp() {
        return getPlayer().getExp();
    }


    public void setExp(float v) {
        getPlayer().setExp(v);
    }

    public void setLevel(int i) {
        getPlayer().setLevel(i);
    }

    public int getFoodLevel() {
        return getPlayer().getFoodLevel();
    }

    public void setFoodLevel(int i) {
        getPlayer().setFoodLevel(i);
    }

    public boolean getAllowFlight() {
        return getPlayer().getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        getPlayer().setAllowFlight(b);
    }

    public void hidePlayer(Plugin plugin, Player player) {
        getPlayer().hidePlayer(plugin, player);
    }

    public void showPlayer(Plugin plugin, Player player) {
        getPlayer().showPlayer(plugin, player);
    }

    public boolean canSee(Player player) {
        return getPlayer().canSee(player);
    }

    public boolean isFlying() {
        return getPlayer().isFlying();
    }

    public void setFlying(boolean b) {
        getPlayer().setFlying(b);
    }

    public float getFlySpeed() {
        return getPlayer().getFlySpeed();
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        getPlayer().setFlySpeed(v);
    }

    public float getWalkSpeed() {
        return getPlayer().getWalkSpeed();
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        getPlayer().setWalkSpeed(v);
    }

    public boolean isHealthScaled() {
        return getPlayer().isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        getPlayer().setHealthScaled(b);
    }

    public double getHealthScale() {
        return getPlayer().getHealthScale();
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        getPlayer().setHealthScale(v);
    }

    public Entity getSpectatorTarget() {
        return getPlayer().getSpectatorTarget();
    }

    public void setSpectatorTarget(Entity entity) {
        getPlayer().setSpectatorTarget(entity);
    }

    public void sendTitle(String s, String s1, int i, int i1, int i2) {
        getPlayer().sendTitle(s, s1, i, i1, i2);
    }

    public void resetTitle() {
        getPlayer().resetTitle();
    }

    public int getClientViewDistance() {
        return getPlayer().getClientViewDistance();
    }

    public String getLocale() {
        return getPlayer().getLocale();
    }

    public void updateCommands() {
        getPlayer().updateCommands();
    }

    public void openBook(ItemStack itemStack) {
        getPlayer().openBook(itemStack);
    }

    public Player.Spigot spigot() {
        return getPlayer().spigot();
    }

    public void sendMessage(String s) {
        getPlayer().sendMessage(s);
    }

    public void sendMessage(String[] s) {
        getPlayer().sendMessage(s);
    }

    public Inventory getInventory() {
        return getPlayer().getInventory();
    }

    public boolean hasPermission(String s) {
        return getPlayer().hasPermission(s);
    }

    public String getName() {
        return getPlayer().getName();
    }

    public void teleport(Location loc) {
        getPlayer().teleport(loc);
    }

    public void openInventory(Inventory inventory) {
        getPlayer().openInventory(inventory);
    }

    public Inventory getEnderChest() {
        return getPlayer().getEnderChest();
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public GameMode getGameMode() {
        return getPlayer().getGameMode();
    }

    public void setGameMode(GameMode mode) {
        getPlayer().setGameMode(mode);
    }
}
