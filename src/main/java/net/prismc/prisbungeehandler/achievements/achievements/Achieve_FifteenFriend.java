package net.prismc.prisbungeehandler.achievements.achievements;

import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementCategories;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementRarity;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class Achieve_FifteenFriend extends AchievementHandler {

    public Achieve_FifteenFriend() {
        PrisBungeeHandler.getInstance().achievementHandlers.add(this);
    }

    @Override
    public String getName() {
        return "FifteenFriend";
    }

    @Override
    public AchievementCategories getType() {
        return AchievementCategories.GENERAL;
    }

    @Override
    public AchievementRarity getRarity() {
        return AchievementRarity.UNCOMMON;
    }

    @Override
    public int getXpAward() {
        return 500;
    }

    @Override
    public void checkAndExecute(PrisPlayer p) {
        if (!p.hasAchievement(getName())) {
            if (p.getFriendCache().size() >= 15) {
                p.addAchievement(getName());
            }
        }
    }
}
