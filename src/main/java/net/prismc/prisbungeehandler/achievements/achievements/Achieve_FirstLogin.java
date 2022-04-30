package net.prismc.prisbungeehandler.achievements.achievements;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementCategories;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementRarity;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.util.concurrent.TimeUnit;

public class Achieve_FirstLogin extends AchievementHandler {

    public Achieve_FirstLogin() {
        PrisBungeeHandler.getInstance().achievementHandlers.add(this);
    }

    @Override
    public String getName() {
        return "FirstJoin";
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
        return 25;
    }

    @Override
    public void checkAndExecute(PrisPlayer p) {
        ProxyServer.getInstance().getScheduler().schedule(PrisBungeeHandler.getInstance(), () -> {
            if (!p.hasAchievement(getName()))
                p.addAchievement(getName());
        }, 10, TimeUnit.SECONDS);
    }
}
