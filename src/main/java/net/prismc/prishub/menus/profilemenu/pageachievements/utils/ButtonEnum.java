package net.prismc.prishub.menus.profilemenu.pageachievements.utils;

import net.prismc.priscore.utils.PatternCollection;

import java.util.regex.Pattern;

public enum ButtonEnum {
    BUTTON_1(PatternCollection.ACHIEVEMENT_1_PATTERN, 1),
    BUTTON_2(PatternCollection.ACHIEVEMENT_2_PATTERN, 2),
    BUTTON_3(PatternCollection.ACHIEVEMENT_3_PATTERN, 3),
    BUTTON_4(PatternCollection.ACHIEVEMENT_4_PATTERN, 4),
    BUTTON_5(PatternCollection.ACHIEVEMENT_5_PATTERN, 5),
    BUTTON_6(PatternCollection.ACHIEVEMENT_6_PATTERN, 6),
    BUTTON_7(PatternCollection.ACHIEVEMENT_7_PATTERN, 7),
    BUTTON_8(PatternCollection.ACHIEVEMENT_8_PATTERN, 8),
    BUTTON_9(PatternCollection.ACHIEVEMENT_9_PATTERN, 9),
    BUTTON_10(PatternCollection.ACHIEVEMENT_10_PATTERN, 10),
    BUTTON_11(PatternCollection.ACHIEVEMENT_11_PATTERN, 11),
    BUTTON_12(PatternCollection.ACHIEVEMENT_12_PATTERN, 12);

    private final Pattern pattern;
    private final int num;

    ButtonEnum(Pattern pattern, int num) {
        this.pattern = pattern;
        this.num = num;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getNum() {
        return num;
    }
}
