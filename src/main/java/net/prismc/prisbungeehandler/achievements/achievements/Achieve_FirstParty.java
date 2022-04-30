package net.prismc.prisbungeehandler.achievements.achievements;

import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementCategories;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementRarity;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class Achieve_FirstParty extends AchievementHandler {

    public Achieve_FirstParty() {
        PrisBungeeHandler.getInstance().achievementHandlers.add(this);
    }

    @Override
    public String getName() {
        return "FirstParty";
    }

    @Override
    public AchievementCategories getType() {
        return AchievementCategories.GENERAL;
    }

    @Override
    public AchievementRarity getRarity() {
        return AchievementRarity.COMMON;
    }

    @Override
    public int getXpAward() {
        return 100;
    }

    @Override
    public void checkAndExecute(PrisPlayer p) {
        if (!p.hasAchievement(getName())) {
            if (p.toOfflinePlayer().getParty() != null) {
                if (p.toOfflinePlayer().getParty().getLeader() == p.toOfflinePlayer()) {
                    p.addAchievement(getName());
                }
            }
        }
    }
}
