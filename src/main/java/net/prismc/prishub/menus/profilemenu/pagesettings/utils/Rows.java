package net.prismc.prishub.menus.profilemenu.pagesettings.utils;

public enum Rows {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    final int num;

    Rows(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
