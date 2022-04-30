package net.prismc.prisbungeehandler.achievements.utils;

import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public abstract class AchievementHandler {

    public abstract String getName();

    public abstract AchievementCategories getType();

    public abstract AchievementRarity getRarity();

    public abstract int getXpAward();

    public abstract void checkAndExecute(PrisPlayer p);

}
