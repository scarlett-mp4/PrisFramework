package net.prismc.prishub.menus.profilemenu.pageachievements.pages;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.api.AchievementParseApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.menus.profilemenu.pageachievements.utils.ButtonEnum;
import net.prismc.prishub.menus.profilemenu.pageachievements.utils.PageType;
import net.prismc.prishub.menus.profilemenu.pageachievements.utils.TitleCreator;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementView {

    public void open(PrisBukkitPlayer p, String category) {
        ChestGui gui = new ChestGui(6, "");
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 5);
        List<AchievementParseApi> categoryAchievements = PrisCore.getInstance().achievementHandler.getCategoryAchievements(category);
        Map<Integer, String> pageTitles = new HashMap<>();
        GuiItem back = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "GUIBasics.Back")), inventoryClickEvent -> new AchievementHome().open(p));
        int maxPages = (int) Math.ceil((double) categoryAchievements.size() / 12.0);
        if (maxPages == 0) {
            maxPages = 1;
        }

        for (int i = 0; i < maxPages; i++) {
            String pageTitle = TitleCreator.getTitle(p, getPageType(category));
            StaticPane pane = new StaticPane(0, 0, 9, 5);
            ArrayList<AchievementParseApi> subAchievements = new ArrayList<>();
            for (int ii = (i * 12); ii <= 11 + (i * 12); ii++) {
                try {
                    subAchievements.add(categoryAchievements.get(ii));
                } catch (Exception ignored) {
                }
            }

            int x = 4;
            for (int ii = 1; ii <= 12; ii++) {
                if (x > 7)
                    x = 4;

                int row;
                if (ii == 1 || ii == 2 || ii == 3 || ii == 4) {
                    row = 1;
                } else if (ii == 5 || ii == 6 || ii == 7 || ii == 8) {
                    row = 2;
                } else {
                    row = 3;
                }

                try {
                    AchievementParseApi achievement = subAchievements.get(ii - 1);
                    ButtonEnum button = getButtonEnum(ii);
                    String rarity = achievement.getRarity().toUpperCase().substring(0, 1);
                    rarity = rarity + achievement.getRarity().toLowerCase().substring(1);
                    pageTitle = button.getPattern().matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Row" + row + "." + rarity));
                    String name = ChatColor.stripColor(UtilApi.getString(p.getLanguageFile(), "Achievements.Listing." + achievement.getName() + ".Name"));
                    name = UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.RarityColors." + rarity) + name;


                    boolean incomplete = true;
                    for (AchievementParseApi completedAchievement : p.getCompletedAchievements()) {
                        List<String> lore = new ArrayList<>();
                        for(String s : UtilApi.getStringList(p.getLanguageFile(), "Achievements.Listing." + achievement.getName() + ".Description")){
                            String str = PatternCollection.XP_PATTERN.matcher(s).replaceAll(String.valueOf(achievement.getXp()));
                            str = PatternCollection.RARITY_PATTERN.matcher(str).replaceAll(UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities." + rarity));
                            str = PatternCollection.UNLOCKED_PATTERN.matcher(str).replaceAll(UtilApi.getString(p.getLanguageFile(), "Achievements.General.Unlocked.YesGUI"));
                            lore.add(str);
                        }

                        if (completedAchievement.getName().equalsIgnoreCase(achievement.getName())) {
                            pane.addItem(new GuiItem(UtilApi.createItem(Material.GREEN_DYE, name, lore)), x, row);
                            incomplete = false;
                        }
                    }

                    if (incomplete) {
                        List<String> lore = new ArrayList<>();
                        for(String s : UtilApi.getStringList(p.getLanguageFile(), "Achievements.Listing." + achievement.getName() + ".Description")){
                            String str = PatternCollection.XP_PATTERN.matcher(s).replaceAll(String.valueOf(achievement.getXp()));
                            str = PatternCollection.RARITY_PATTERN.matcher(str).replaceAll(UtilApi.getString(p.getLanguageFile(), "Achievements.General.Rarities." + rarity));
                            str = PatternCollection.UNLOCKED_PATTERN.matcher(str).replaceAll(UtilApi.getString(p.getLanguageFile(), "Achievements.General.Unlocked.NoGUI"));
                            lore.add(str);
                        }

                        pane.addItem(new GuiItem(UtilApi.createItem(Material.RED_DYE, name, lore)), x, row);
                    }

                } catch (Exception e) {
                    ButtonEnum button = getButtonEnum(ii);
                    pageTitle = button.getPattern().matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Row" + row + ".Unknown"));
                }
                x++;
            }

            int finalI = i;
            GuiItem right = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Next")), inventoryClickEvent -> {
                paginatedPane.setPage(finalI + 1);
                gui.setTitle(pageTitles.get(finalI + 1));
                gui.update();
            });
            GuiItem left = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Previous")), inventoryClickEvent -> {
                paginatedPane.setPage(finalI - 1);
                gui.setTitle(pageTitles.get(finalI - 1));
                gui.update();
            });

            if (i == 0) {
                pageTitle = PatternCollection.LEFT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.NoLeft"));
                if (maxPages == 1) {
                    pageTitle = PatternCollection.RIGHT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.NoRight"));
                } else {
                    pageTitle = PatternCollection.RIGHT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.YesRight"));
                    pane.addItem(right, 7, 4);
                }
            } else if (i == maxPages - 1) {
                pageTitle = PatternCollection.RIGHT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.NoRight"));
                pageTitle = PatternCollection.LEFT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.YesLeft"));
                pane.addItem(left, 4, 4);
            } else {
                pageTitle = PatternCollection.LEFT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.YesLeft"));
                pageTitle = PatternCollection.RIGHT_ARROW.matcher(pageTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.AchievementsPage.Types.Arrows.YesRight"));
                pane.addItem(right, 7, 4);
                pane.addItem(left, 4, 4);
            }

            pane.addItem(back, 5, 4);
            pane.addItem(back, 6, 4);
            pageTitles.put(i, pageTitle);
            paginatedPane.addPane(i, pane);
        }

        // Final
        paginatedPane.setPage(0);
        gui.setTitle(pageTitles.get(0));
        gui.addPane(paginatedPane);
        gui.addPane(ProfileMenuUtils.createMenuBar(p));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.show(p.getPlayer());
    }

    private ButtonEnum getButtonEnum(int value) {
        for (ButtonEnum e : ButtonEnum.values()) {
            if (e.getNum() == value) {
                return e;
            }
        }
        return null;
    }

    private PageType getPageType(String value) {
        for (PageType pageType : PageType.values()) {
            if (pageType.getName().equalsIgnoreCase(value)) {
                return pageType;
            }
        }
        return null;
    }
}