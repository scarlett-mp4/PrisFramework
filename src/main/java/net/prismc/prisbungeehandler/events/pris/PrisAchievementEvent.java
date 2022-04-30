package net.prismc.prisbungeehandler.events.pris;

import net.md_5.bungee.api.plugin.Event;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

public class PrisAchievementEvent extends Event {

    private final PrisPlayer player;
    private final AchievementHandler achievement;

    public PrisAchievementEvent(PrisPlayer player, AchievementHandler achievement) {
        this.player = player;
        this.achievement = achievement;
    }

    public PrisPlayer getPlayer() {
        return player;
    }

    public AchievementHandler getAchievement() {
        return achievement;
    }
}
