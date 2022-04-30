package net.prismc.prishub.menus.profilemenu.pageachievements.utils;

public enum PageType {
    HOME("home"),
    BEDWARS("bedwars"),
    SKYWARS("skywars"),
    EVENTS("events"),
    GENERAL("general");

    private final String name;

    PageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
