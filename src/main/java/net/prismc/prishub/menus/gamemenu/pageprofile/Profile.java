package net.prismc.prishub.menus.gamemenu.pageprofile;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Profile {

    public void open(PrisBukkitPlayer p) {
        ChestGui gui = new ChestGui(6, PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.ProfilePage.Title")).
                replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.ProfilePage.ChestTitle")));
        StaticPane pane = new StaticPane(0, 0, 9, 6);
        ItemStack alphaStack = UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.ProfilePage.Alpha.Title"),
                UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.ProfilePage.Alpha.Lore"));


    }
}
