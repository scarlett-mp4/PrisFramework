package net.prismc.prisbungeehandler.utils;

import java.util.regex.Pattern;

public final class PatternCollection {
    // Patterns
    public static final Pattern PLAYER_PATTERN = Pattern.compile("[PLAYER]", Pattern.LITERAL);
    public static final Pattern PAGE_PATTERN = Pattern.compile("[PAGE]", Pattern.LITERAL);
    public static final Pattern MAXPAGES_PATTERN = Pattern.compile("[MAXPAGES]", Pattern.LITERAL);
    public static final Pattern STATUS_PATTERN = Pattern.compile("[STATUS]", Pattern.LITERAL);
    public static final Pattern MESSAGE_PATTERN = Pattern.compile("[MESSAGE]", Pattern.LITERAL);
    public static final Pattern MEMBERS_PATTERN = Pattern.compile("[MEMBERS]", Pattern.LITERAL);
    public static final Pattern MOD_PATTERN = Pattern.compile("[MOD]", Pattern.LITERAL);
    public static final Pattern STRING_PATTERN = Pattern.compile("[STRING]", Pattern.LITERAL);
    public static final Pattern EXACT_PATTERN = Pattern.compile("[EXACT]", Pattern.LITERAL);
}
