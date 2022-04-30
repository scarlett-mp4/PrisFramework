package net.prismc.prisbungeehandler.achievements.achievements;

import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementCategories;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementRarity;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class Achieve_TwentyFourHour extends AchievementHandler {

    public Achieve_TwentyFourHour() {
        PrisBungeeHandler.getInstance().achievementHandlers.add(this);
    }

    @Override
    public String getName() {
        return "TwentyFourHour";
    }

    @Override
    public AchievementCategories getType() {
        return AchievementCategories.GENERAL;
    }

    @Override
    public AchievementRarity getRarity() {
        return AchievementRarity.EPIC;
    }

    @Override
    public int getXpAward() {
        return 5000;
    }

    @Override
    public void checkAndExecute(PrisPlayer p) {
        if (!p.hasAchievement(getName())) {
            if (p.getSessionTime() >= 86400) {
                p.addAchievement(getName());
            }
        }
    }
}
