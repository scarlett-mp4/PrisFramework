package net.prismc.priscore.achievements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.AchievementParseApi;
import net.prismc.priscore.events.PrisAchievementListReceived;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AchievementHandler implements Listener {

    private final List<JsonObject> achievements = new ArrayList<>();

    public AchievementHandler() {
        Bukkit.getScheduler().runTaskTimer(PrisCore.getInstance(), () -> {
            achievementCheck();
        }, 10, 6000);
    }

    private void achievementCheck() {
        Bukkit.getScheduler().runTaskAsynchronously(PrisCore.getInstance(), () -> {
            JsonObject object = new JsonObject();
            object.addProperty("subchannel", "achievementList");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF(object.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.getServer().sendPluginMessage(PrisCore.getInstance(), PrisCore.BUNGEE_CHANNEL, stream.toByteArray());
        });
    }

    public List<JsonObject> getRawAchievements() {
        return achievements;
    }

    public List<AchievementParseApi> getParsedAchievements() {
        List<AchievementParseApi> list = new ArrayList<>();
        for (JsonObject object : achievements) {
            list.add(new AchievementParseApi(object));
        }

        list.sort(Comparator.comparing(AchievementParseApi::getOrder));
        return list;
    }

    public List<AchievementParseApi> getCategoryAchievements(String value) {
        List<AchievementParseApi> list = new ArrayList<>();
        for (JsonObject object : achievements) {
            AchievementParseApi achievementParseApi = new AchievementParseApi(object);
            if (achievementParseApi.getCategory().equalsIgnoreCase(value)) {
                list.add(achievementParseApi);
            }
        }

        list.sort(Comparator.comparing(AchievementParseApi::getOrder));
        return list;
    }

    public AchievementParseApi getAchievement(String name) {
        for (AchievementParseApi achievement : getParsedAchievements()) {
            if (achievement.getName().equalsIgnoreCase(name)) {
                return achievement;
            }
        }
        return null;
    }

    @EventHandler
    private void onLogin(PlayerLoginEvent e) {
        if (achievements.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(PrisCore.getInstance(), this::achievementCheck, 20);
        }
    }

    @EventHandler
    private void onReceive(PrisAchievementListReceived e) {
        if (!achievements.isEmpty()) {
            achievements.clear();
        }

        JsonArray array = e.getObject().getAsJsonArray("achievements");
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            achievements.add(object);
        }
    }
}
