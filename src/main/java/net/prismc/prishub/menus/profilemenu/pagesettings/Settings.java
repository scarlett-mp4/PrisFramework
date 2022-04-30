package net.prismc.prishub.menus.profilemenu.pagesettings;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.menus.profilemenu.pagesettings.utils.ButtonGrabber;
import net.prismc.prishub.menus.profilemenu.pagesettings.utils.Buttons;
import net.prismc.prishub.menus.profilemenu.pagesettings.utils.Rows;
import net.prismc.prishub.menus.profilemenu.pagesettings.utils.SettingObject;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    public void open(PrisBukkitPlayer p) {
        ChestGui gui = new ChestGui(6, "");
        StaticPane pane = new StaticPane(0, 0, 9, 5);
        List<SettingObject> list = new ArrayList<>();

        // Settings
        SettingObject friendRequests = new SettingObject(p, "FriendRequests", 1, p.getSetting(0), Rows.ONE, Buttons.OFF, Buttons.ON, null, PatternCollection.SETTING_1_PATTERN, 0);
        SettingObject friendNotifications = new SettingObject(p, "FriendNotifications", 5, p.getSetting(1), Rows.ONE, Buttons.OFF, Buttons.ON, null, PatternCollection.SETTING_2_PATTERN, 1);
        SettingObject msgToggle = new SettingObject(p, "MsgToggle", 1, p.getSetting(2), Rows.TWO, Buttons.OFF, Buttons.PALS, Buttons.ON, PatternCollection.SETTING_3_PATTERN, 2);
        SettingObject chatFilter = new SettingObject(p, "ChatFilter", 5, p.getSetting(3), Rows.TWO, Buttons.LOW, Buttons.MEDIUM, Buttons.HIGH, PatternCollection.SETTING_4_PATTERN, 3);
        SettingObject partyInvites = new SettingObject(p, "PartyInvites", 1, p.getSetting(4), Rows.THREE, Buttons.OFF, Buttons.PALS, Buttons.ON, PatternCollection.SETTING_5_PATTERN, 4);
        list.add(friendRequests);
        list.add(friendNotifications);
        list.add(msgToggle);
        list.add(chatFilter);
        list.add(partyInvites);

        // Buttons
        for (SettingObject object : list) {
            GuiItem item = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Settings.Buttons." + object.getName() + ".Title")
                    , UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.Settings.Buttons." + object.getName() + ".Lore")), inventoryClickEvent -> {
                if (object.getOption_2() == null) {
                    if (object.getNewValue() >= 1) {
                        object.setNewValue(0);
                    } else {
                        object.setNewValue(object.getNewValue() + 1);
                    }
                } else {
                    if (object.getNewValue() >= 2) {
                        object.setNewValue(0);
                    } else {
                        object.setNewValue(object.getNewValue() + 1);
                    }
                }
                gui.setTitle(setTitle(p, list));
                gui.update();
            });
            pane.addItem(item, object.getStartPosition(), object.getRow().getNum() - 1);
            pane.addItem(item, object.getStartPosition() + 1, object.getRow().getNum() - 1);
            pane.addItem(item, object.getStartPosition() + 2, object.getRow().getNum() - 1);
        }

        // Final
        gui.setTitle(setTitle(p, list));
        gui.addPane(pane);
        gui.addPane(ProfileMenuUtils.createMenuBar(p));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.setOnClose(inventoryCloseEvent -> {
            for (SettingObject object : list) {
                if (!object.compareValues()) {
                    p.setSpecificSetting(object.getId(), String.valueOf(object.getNewValue()), true);
                }
            }
        });
        gui.show(p.getPlayer());
    }

    private String setTitle(PrisBukkitPlayer p, List<SettingObject> list) {
        String title = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Settings.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Settings.ChestTitle"));
        title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.Settings.Layout"));

        for (SettingObject object : list) {
            if (object.getNewValue() == 0) {
                title = object.getPattern().matcher(title).replaceAll(object.getOption_0());
            } else if (object.getNewValue() == 1) {
                title = object.getPattern().matcher(title).replaceAll(object.getOption_1());
            } else {
                title = object.getPattern().matcher(title).replaceAll(object.getOption_2());
            }
        }

        title = PatternCollection.SETTING_6_PATTERN.matcher(title).replaceAll(ButtonGrabber.getButton(p.getLanguageFile(), Buttons.BLANK, Rows.THREE));
        title = PatternCollection.SETTING_7_PATTERN.matcher(title).replaceAll(ButtonGrabber.getButton(p.getLanguageFile(), Buttons.BLANK, Rows.FOUR));
        title = PatternCollection.SETTING_8_PATTERN.matcher(title).replaceAll(ButtonGrabber.getButton(p.getLanguageFile(), Buttons.BLANK, Rows.FOUR));
        title = PatternCollection.SETTING_9_PATTERN.matcher(title).replaceAll(ButtonGrabber.getButton(p.getLanguageFile(), Buttons.BLANK, Rows.FIVE));
        title = PatternCollection.SETTING_10_PATTERN.matcher(title).replaceAll(ButtonGrabber.getButton(p.getLanguageFile(), Buttons.BLANK, Rows.FIVE));

        return title;
    }
}