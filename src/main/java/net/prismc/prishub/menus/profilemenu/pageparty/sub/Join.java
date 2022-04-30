package net.prismc.prishub.menus.profilemenu.pageparty.sub;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

public class Join {

    public void open(PrisBukkitPlayer p) {
        String title = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.JoinPage.Title")).
                replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.JoinPage.ChestTitle"));
        title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.JoinPage.Layout"));
        AnvilGui gui = new AnvilGui(title);
        StaticPane cancel = new StaticPane(0, 0, 1, 1);
        StaticPane confirm = new StaticPane(0, 0, 1, 1);

        cancel.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, ChatColor.translateAlternateColorCodes('&', "&7"),
                UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.JoinPage.Cancel")), inventoryClickEvent -> {
            p.getPlayer().closeInventory();
        }), 0, 0);
        confirm.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, ChatColor.translateAlternateColorCodes('&', "&7"),
                UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.JoinPage.Confirm")), inventoryClickEvent -> {
            String partyLeader = gui.getRenameText();
            partyLeader = partyLeader.replaceAll(" ", "_");
            if (partyLeader.isEmpty()) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            } else {
                p.getPlayer().closeInventory();
                p.joinParty(partyLeader);
            }
            if (!gui.isUpdating()) {
                gui.update();
            }
        }), 0, 0);

        gui.getFirstItemComponent().addPane(cancel);
        gui.getResultComponent().addPane(confirm);
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.show(p.getPlayer());
    }
}
