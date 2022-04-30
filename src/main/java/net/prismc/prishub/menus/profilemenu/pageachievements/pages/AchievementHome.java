package net.prismc.prishub.menus.profilemenu.pageachievements.pages;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.AchievementParseApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.menus.profilemenu.pageachievements.utils.PageType;
import net.prismc.prishub.menus.profilemenu.pageachievements.utils.TitleCreator;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class AchievementHome {

    public void open(PrisBukkitPlayer p) {
        ChestGui gui = new ChestGui(6, "");
        StaticPane pane = new StaticPane(0, 0, 9, 5);

        List<String> generalLore = new ArrayList<>();
        List<String> eventLore = new ArrayList<>();
        List<String> bedwarsLore = new ArrayList<>();

        for (String s : UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.AchievementsPage.PageNames.General.Lore")) {
            List<AchievementParseApi> total = PrisCore.getInstance().achievementHandler.getCategoryAchievements("general");
            int completed = 0;
            for (AchievementParseApi api : p.getCompletedAchievements()) {
                if (api.getCategory().equalsIgnoreCase("general")) {
                    completed++;
                }
            }

            s = PatternCollection.UNLOCKED_PATTERN.matcher(s).replaceAll(String.valueOf(completed));
            s = PatternCollection.TOTAL_PATTERN.matcher(s).replaceAll(String.valueOf(total.size()));
            generalLore.add(s);
        }

        for (String s : UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.AchievementsPage.PageNames.Events.Lore")) {
            List<AchievementParseApi> total = PrisCore.getInstance().achievementHandler.getCategoryAchievements("event");
            int completed = 0;
            for (AchievementParseApi api : p.getCompletedAchievements()) {
                if (api.getCategory().equalsIgnoreCase("events")) {
                    completed++;
                }
            }

            s = PatternCollection.UNLOCKED_PATTERN.matcher(s).replaceAll(String.valueOf(completed));
            s = PatternCollection.TOTAL_PATTERN.matcher(s).replaceAll(String.valueOf(total.size()));
            eventLore.add(s);
        }

        for (String s : UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.AchievementsPage.PageNames.Bedwars.Lore")) {
            List<AchievementParseApi> total = PrisCore.getInstance().achievementHandler.getCategoryAchievements("bedwars");
            int completed = 0;
            for (AchievementParseApi api : p.getCompletedAchievements()) {
                if (api.getCategory().equalsIgnoreCase("bedwars")) {
                    completed++;
                }
            }

            s = PatternCollection.UNLOCKED_PATTERN.matcher(s).replaceAll(String.valueOf(completed));
            s = PatternCollection.TOTAL_PATTERN.matcher(s).replaceAll(String.valueOf(total.size()));
            bedwarsLore.add(s);
        }


        pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.PageNames.General.Title"), generalLore), inventoryClickEvent -> {
            new AchievementView().open(p, "general");
        }), 4, 2);
        pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.PageNames.Events.Title"), eventLore), inventoryClickEvent -> {
            new AchievementView().open(p, "events");
        }), 5, 2);
        pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.PageNames.Bedwars.Title"), bedwarsLore), inventoryClickEvent -> {
            new AchievementView().open(p, "bedwars");
        }), 6, 2);


        // Final
        gui.setTitle(TitleCreator.getTitle(p, PageType.HOME));
        gui.addPane(pane);
        gui.addPane(ProfileMenuUtils.createMenuBar(p));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.show(p.getPlayer());
    }
}
