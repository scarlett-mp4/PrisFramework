package net.prismc.prishub.menus.profilemenu.pageprofile;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Profile {

    public void open(PrisBukkitPlayer p, boolean firstOpen) {
        ChestGui gui = new ChestGui(6, PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.ProfilePage.Title")).
                replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.ProfilePage.ChestTitle")));
        StaticPane pane = new StaticPane(0, 0, 9, 6);
        ItemStack alphaStack = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.ProfilePage.Alpha.Title"),
                UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.ProfilePage.Alpha.Lore"));

        if (firstOpen) {
            p.fetchFriendList().whenComplete(((object, throwable) -> {
                p.setTempFriendObject(object);
                pane.addItem(new GuiItem(UtilApi.createPlayerHead(p)), 2, 2);
                pane.addItem(new GuiItem(alphaStack), 3, 4);
                pane.addItem(new GuiItem(alphaStack), 4, 4);
                pane.addItem(new GuiItem(alphaStack), 5, 4);
                gui.setOnGlobalClick(event -> event.setCancelled(true));
                gui.addPane(pane);
                gui.addPane(ProfileMenuUtils.createMenuBar(p));
                gui.show(p.getPlayer());
            }));
        } else {
            pane.addItem(new GuiItem(UtilApi.createPlayerHead(p)), 2, 2);
            pane.addItem(new GuiItem(alphaStack), 3, 4);
            pane.addItem(new GuiItem(alphaStack), 4, 4);
            pane.addItem(new GuiItem(alphaStack), 5, 4);
            gui.setOnGlobalClick(event -> event.setCancelled(true));
            gui.addPane(pane);
            gui.addPane(ProfileMenuUtils.createMenuBar(p));
            gui.show(p.getPlayer());
        }
    }
}
