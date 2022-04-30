package net.prismc.priscore.utils;

public enum NumInfo {
    NUM_0(0, "\uF19C"),
    NUM_1(1, "\uF191"),
    NUM_2(2, "\uF192"),
    NUM_3(3, "\uF193"),
    NUM_4(4, "\uF194"),
    NUM_5(5, "\uF195"),
    NUM_6(6, "\uF196"),
    NUM_7(7, "\uF197"),
    NUM_8(8, "\uF198"),
    NUM_9(9, "\uF199");

    private final int number;
    private final String unicode;

    NumInfo(int number, String unicode) {

        this.number = number;
        this.unicode = unicode;
    }

    public int getNumber() {
        return number;
    }

    public String getUnicode() {
        return unicode;
    }
}
