package net.prismc.priscore.api;

import com.google.gson.JsonObject;

public class AchievementParseApi {
    private final JsonObject object;

    public AchievementParseApi(JsonObject object) {
        this.object = object;
    }

    public String getName() {
        return object.get("name").getAsString();
    }

    public String getCategory() {
        return object.get("type").getAsString();
    }

    public String getRarity() {
        return object.get("rarity").getAsString();
    }

    public int getXp() {
        return object.get("xp").getAsInt();
    }

    public int getOrder() {
        switch (object.get("rarity").getAsString().toLowerCase()) {
            case "common":
                return 0;
            case "uncommon":
                return 1;
            case "rare":
                return 2;
            case "epic":
                return 3;
            case "legendary":
                return 4;
        }

        return 0;
    }
}
