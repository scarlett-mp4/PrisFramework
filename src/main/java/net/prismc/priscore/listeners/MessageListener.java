package net.prismc.priscore.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.events.PrisAchievementEvent;
import net.prismc.priscore.events.PrisAchievementListReceived;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

/**
 * Manages incoming and outgoing plugin messages
 */
public class MessageListener implements PluginMessageListener {

    // Variables
    private final Gson GSON = new Gson();

    /**
     * Sends the specified player to the specified server
     *
     * @param p - Specify a player.
     * @param s - Specify a server to send the player to.
     */
    public void connect(Player p, String s) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(s);
        p.sendPluginMessage(PrisCore.getInstance(), "BungeeCord", out.toByteArray());
    }

    /**
     * Manages the incoming plugin messages
     *
     * @param channel - The channel the message was received from.
     * @param player  - The player received from the message.
     * @param message - The action received from the message.
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(PrisCore.BUNGEE_CHANNEL)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            JsonObject object = GSON.fromJson(in.readUTF(), JsonObject.class);
            String subChannel = object.get("subchannel").getAsString().replaceAll("\"", "");

            try {
                if (subChannel.equals("playerupdate")) {
                    PrisBukkitPlayer prisBukkitPlayer = new PrisBukkitPlayer(object);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "PrisBukkitPlayer: " + prisBukkitPlayer.getUsername());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                if (subChannel.equals("generalupdate")) {
                    if (object.get("start").getAsString().equals("proxy")) {
                        PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                        prisBukkitPlayer.setPrisLevel(object.get("level").getAsInt(), false);
                        prisBukkitPlayer.setProgress(object.get("progress").getAsInt(), false);
                        prisBukkitPlayer.setLang(object.get("lang").getAsString(), false);
                        prisBukkitPlayer.setSettings(object.get("settings").getAsString(), false);
                        prisBukkitPlayer.setPriscoins(object.get("priscoins").getAsInt(), false);
                        prisBukkitPlayer.setRubies(object.get("rubies").getAsInt(), false);
                        prisBukkitPlayer.setKarma(object.get("karma").getAsInt(), false);
                    }
                }
            } catch (Exception ignore) {
            }
            try {
                if (subChannel.equals("partyupdate")) {
                    PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                    prisBukkitPlayer.setParty(object.get("party").getAsJsonObject());
                }
            } catch (Exception ignore) {
            }

            try {
                if (subChannel.equals("getfriendlist")) {
                    PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                    prisBukkitPlayer.getFuture().complete(object);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                if (subChannel.equals("achievementevent")) {
                    PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                    Bukkit.getPluginManager().callEvent(new PrisAchievementEvent(prisBukkitPlayer, object));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                if (subChannel.equals("achievementList")) {
                    Bukkit.getPluginManager().callEvent(new PrisAchievementListReceived(object));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                if (subChannel.equals("completedAchievements")) {
                    try {
                        PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                        prisBukkitPlayer.clearCompletedAchievementsCache();
                        JsonArray array = object.getAsJsonArray("achievements");
                        for (int i = 0; i < array.size(); i++) {
                            String s = array.get(i).getAsString();
                            prisBukkitPlayer.addCompletedAchievementsCache(s);
                        }
                    } catch (Exception e) {
                        try {
                            Bukkit.getScheduler().runTaskLater(PrisCore.getInstance(), () -> {
                                PrisBukkitPlayer prisBukkitPlayer = PrisCoreApi.wrapPrisBukkitPlayer(Bukkit.getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                                prisBukkitPlayer.clearCompletedAchievementsCache();
                                JsonArray array = object.getAsJsonArray("achievements");
                                for (int i = 0; i < array.size(); i++) {
                                    String s = array.get(i).getAsString();
                                    prisBukkitPlayer.addCompletedAchievementsCache(s);
                                }
                            }, 60);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
