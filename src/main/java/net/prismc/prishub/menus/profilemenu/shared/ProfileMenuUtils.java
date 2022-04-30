package net.prismc.prishub.menus.profilemenu.shared;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.prishub.menus.profilemenu.pageachievements.pages.AchievementHome;
import net.prismc.prishub.menus.profilemenu.pagefriends.Friends;
import net.prismc.prishub.menus.profilemenu.pagelanguage.Language;
import net.prismc.prishub.menus.profilemenu.pageparty.Party;
import net.prismc.prishub.menus.profilemenu.pageprofile.Profile;
import net.prismc.prishub.menus.profilemenu.pagesettings.Settings;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ProfileMenuUtils {

    public static StaticPane createMenuBar(PrisBukkitPlayer player) {
        StaticPane pane = new StaticPane(0, 5, 9, 1);
        ItemStack you = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(player.getLanguageFile(), "ProfileMenu.MenuBar.You"));
        ItemStack friends = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(player.getLanguageFile(), "ProfileMenu.MenuBar.Friends"));
        ItemStack party = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(player.getLanguageFile(), "ProfileMenu.MenuBar.Party"));
        ItemStack achievements = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(player.getLanguageFile(), "ProfileMenu.MenuBar.Achievements"));
        ItemStack settings = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(player.getLanguageFile(), "ProfileMenu.MenuBar.Settings"));
        ItemStack language = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(player.getLanguageFile(), "ProfileMenu.MenuBar.Language"));

        pane.addItem(new GuiItem(you, inventoryClickEvent -> new Profile().open(player, false)), 1, 0);
        pane.addItem(new GuiItem(you, inventoryClickEvent -> new Profile().open(player, false)), 2, 0);
        pane.addItem(new GuiItem(friends, inventoryClickEvent -> new Friends().open(player)), 3, 0);
        pane.addItem(new GuiItem(party, inventoryClickEvent -> new Party().open(player)), 4, 0);
        pane.addItem(new GuiItem(achievements, inventoryClickEvent -> new AchievementHome().open(player)), 5, 0);
        pane.addItem(new GuiItem(settings, inventoryClickEvent -> new Settings().open(player)), 6, 0);
        pane.addItem(new GuiItem(language, inventoryClickEvent -> new Language().open(player)), 7, 0);
        return pane;
    }
}
