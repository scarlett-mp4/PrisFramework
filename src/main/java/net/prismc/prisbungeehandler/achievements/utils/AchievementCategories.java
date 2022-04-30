package net.prismc.prisbungeehandler.achievements.utils;

public enum AchievementCategories {
    GENERAL("GENERAL"),
    EVENT("EVENT"),
    BEDWARS("BEDWARS");

    private final String string;

    AchievementCategories(String string) {
        this.string = string;
    }

    public String getCategory() {
        return string;
    }
}
