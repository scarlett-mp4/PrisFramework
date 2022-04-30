package net.prismc.prishub.menus.profilemenu.pageachievements.utils;

import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;

public class TitleCreator {

    public static String getTitle(PrisBukkitPlayer p, PageType page) {
        String title = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.ChestTitle"));
        title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Layout"));

        switch (page) {
            case HOME:
                title = PatternCollection.PAGE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Pages.Home"));
                title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Buttons.Home"));
                title = PatternCollection.ACHIEVEMENT_1_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_2_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_3_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_4_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_5_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_6_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_7_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_8_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_9_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_10_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_11_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.ACHIEVEMENT_12_PATTERN.matcher(title).replaceAll("");
                title = PatternCollection.LEFT_ARROW.matcher(title).replaceAll("");
                title = PatternCollection.RIGHT_ARROW.matcher(title).replaceAll("");
                break;
            case BEDWARS:
                title = PatternCollection.PAGE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Pages.Bedwars"));
                title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Buttons.View"));
                break;
            case SKYWARS:
                title = PatternCollection.PAGE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Pages.Skywars"));
                title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Buttons.View"));
                break;
            case EVENTS:
                title = PatternCollection.PAGE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Pages.Events"));
                title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Buttons.View"));
                break;
            case GENERAL:
                title = PatternCollection.PAGE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Pages.General"));
                title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Buttons.View"));
                break;
        }

        return title;
    }
}
