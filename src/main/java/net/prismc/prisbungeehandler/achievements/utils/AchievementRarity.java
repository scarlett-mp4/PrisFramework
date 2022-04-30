package net.prismc.prisbungeehandler.achievements.utils;

public enum AchievementRarity {
    COMMON("COMMON"),
    UNCOMMON("UNCOMMON"),
    RARE("RARE"),
    EPIC("EPIC"),
    LEGENDARY("LEGENDARY");

    private final String string;

    AchievementRarity(String string) {
        this.string = string;
    }

    public String getRarity() {
        return string;
    }
}
