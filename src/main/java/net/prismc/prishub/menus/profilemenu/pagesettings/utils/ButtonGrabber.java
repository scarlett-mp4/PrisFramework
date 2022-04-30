package net.prismc.prishub.menus.profilemenu.pagesettings.utils;

import net.prismc.priscore.api.UtilApi;
import org.bukkit.configuration.file.FileConfiguration;

public class ButtonGrabber {

    public static String getButton(FileConfiguration languageFile, Buttons button, Rows rowType) {
        int row = rowType.getNum();
        switch (button) {
            case ON:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(0));
            case OFF:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(1));
            case PALS:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(2));
            case BLANK:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(3));
            case LOW:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(4));
            case MEDIUM:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(5));
            case HIGH:
                return String.valueOf(UtilApi.getString(languageFile, "ProfileMenu.Settings.Toggles.Row" + row).charAt(6));
        }
        return "error!";
    }
}

