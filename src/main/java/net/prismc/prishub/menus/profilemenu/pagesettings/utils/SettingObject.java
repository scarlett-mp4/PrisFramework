package net.prismc.prishub.menus.profilemenu.pagesettings.utils;

import net.prismc.priscore.prisplayer.PrisBukkitPlayer;

import java.util.regex.Pattern;

public class SettingObject {

    private final String name;
    private final int startPosition;
    private final int oldValue;
    private final Rows row;
    private final String option_0;
    private final String option_1;
    private final Pattern pattern;
    private final int id;
    private int newValue;
    private String option_2 = null;

    public SettingObject(PrisBukkitPlayer prisBukkitPlayer, String name, int startPosition, String oldValue, Rows row, Buttons button_0, Buttons button_1, Buttons button_2, Pattern pattern, int id) {
        this.name = name;
        this.startPosition = startPosition;
        this.oldValue = Integer.parseInt(oldValue);
        this.row = row;
        this.pattern = pattern;
        this.id = id;
        this.option_0 = ButtonGrabber.getButton(prisBukkitPlayer.getLanguageFile(), button_0, row);
        this.option_1 = ButtonGrabber.getButton(prisBukkitPlayer.getLanguageFile(), button_1, row);
        this.newValue = Integer.parseInt(oldValue);
        if (button_2 != null)
            this.option_2 = ButtonGrabber.getButton(prisBukkitPlayer.getLanguageFile(), button_2, row);

    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }

    public boolean compareValues() {
        return newValue == oldValue;
    }

    public Rows getRow() {
        return row;
    }

    public String getOption_0() {
        return option_0;
    }

    public String getOption_1() {
        return option_1;
    }

    public String getOption_2() {
        return option_2;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getName() {
        return name;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getId() {
        return id;
    }
}
