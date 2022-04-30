package net.prismc.prishub.menus.profilemenu.pagelanguage;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.Material;

public class Language {

    public void open(PrisBukkitPlayer p) {
        ChestGui gui = new ChestGui(6, "");
        StaticPane pane = new StaticPane(0, 0, 9, 5);
        String title = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.ChestTitle"));
        title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Layout"));

        GuiItem info = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Info.Title"), UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.Language.Info.Lore")));
        GuiItem english = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Languages.English.Title"),
                UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.Language.Languages.English.Lore")), inventoryClickEvent -> {
            p.getPlayer().closeInventory();
            p.setLang("english", true);
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Languages.English.Switched"));
        });
        GuiItem spanish = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Languages.Spanish.Title"),
                UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.Language.Languages.Spanish.Lore")), inventoryClickEvent -> {
            p.getPlayer().closeInventory();
            p.setLang("spanish", true);
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Language.Languages.Spanish.Switched"));
        });

        pane.addItem(english, 1, 2);
        pane.addItem(spanish, 2, 2);

        for (int i = 1; i <= 7; i++)
            pane.addItem(info, i, 1);

        // Final
        gui.setTitle(title);
        gui.addPane(pane);
        gui.addPane(ProfileMenuUtils.createMenuBar(p));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.show(p.getPlayer());
    }
}
