package net.prismc.prisbungeehandler.api;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.ChatFilterLevels;
import net.prismc.prisbungeehandler.utils.DefaultFontInfo;
import net.prismc.prisbungeehandler.utils.LevelObject;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilApi {

    private final static int CENTER_PX = 154;

    public static LevelObject getLevelObject(int level) {
        for (LevelObject object : LevelObject.values()) {
            if (object.getNumber() == level) {
                return object;
            }
        }
        return null;
    }

    public static List<String> getStringList(Configuration f, String path) {
        List<String> output = new ArrayList<>();
        for (String s : f.getStringList(path)) {
            output.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return output;
    }

    public static String getString(Configuration f, String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(f.getString(path)));
    }

    public static int getInt(Configuration f, String path) {
        return f.getInt(path);
    }

    public static double getDouble(Configuration f, String path) {
        return f.getDouble(path);
    }

    public static boolean getBoolean(Configuration f, String path) {
        return f.getBoolean(path);
    }

    public static void sendCenteredMessage(PrisPlayer player, String message) {
        if (message == null || message.equals("")) player.sendMessage(new TextComponent(""));

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(new TextComponent(sb + message));
    }

    public static TextComponent getCenteredMessage(PrisPlayer player, String message) {
        if (message == null || message.equals("")) player.sendMessage(new TextComponent(""));

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return new TextComponent(sb + message);
    }

    public static ComponentBuilder getCenteredMessage(PrisPlayer player, ComponentBuilder builder) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        String message = "";
        for (BaseComponent c : builder.getParts()) {
            message = message + c.toLegacyText();
        }

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        ComponentBuilder newBuilder = new ComponentBuilder();
        newBuilder.append(sb.toString());
        for (BaseComponent c : builder.getParts()) {
            newBuilder.append(c);
        }

        return newBuilder;
    }

    public static void sendCenteredMessage(PrisPlayer player, ComponentBuilder builder) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        String message = "";
        for (BaseComponent c : builder.getParts()) {
            message = message + c.toLegacyText();
        }

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        ComponentBuilder newBuilder = new ComponentBuilder();
        newBuilder.append(sb.toString());
        for (BaseComponent c : builder.getParts()) {
            newBuilder.append(c);
        }

        player.sendMessage(newBuilder.create());
    }

    public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Map.Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static String getTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        return dateTimeFormatter.print(localDateTime);
    }

    public static LuckPerms getLuckPerms() {
        return LuckPermsProvider.get();
    }

    public static String parseSwears(String message, ChatFilterLevels level) {

        switch (level) {
            case LOW:
                return parse(message, "LOW");
            case MEDIUM:
                return parse(message, "MEDIUM");
            case HIGH:
                return parse(message, "HIGH");
        }

        return message;
    }

    private static String parse(String message, String level) {
        List<String> swearList = PrisBungeeHandler.getInstance().config.getConfig().getStringList("Filter." + level);

        String newMessage = "";
        newMessage = phrases(message, swearList);
        newMessage = joinedWords(newMessage, swearList);
        newMessage = splitWords(newMessage, swearList);
        newMessage = urlBlock(newMessage);

        if (newMessage.charAt(0) == ' ') {
            newMessage = newMessage.substring(1);
        }

        return newMessage;
    }

    private static String urlBlock(String message) {
        StringBuilder builder = new StringBuilder();
        String[] args = message.split(" ");
        String urlRegex = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern p = Pattern.compile(urlRegex);

        for (int i = 0; i < args.length; i++) {
            Matcher m = p.matcher(args[i]);
            if (m.find()) {
                args[i] = args[i].replaceAll("\\.", "-");
            }
            builder.append(args[i]).append(" ");
        }

        return builder.toString();
    }

    private static String splitWords(String message, List<String> swearList) {
        String[] stringArgs = message.split(" ");
        StringBuilder newMessage = new StringBuilder();
        ArrayList<String> args = new ArrayList<>();

        for (String s : stringArgs) {
            args.add(s);
        }

        for (String swear : swearList) {
            for (int i = 0; i < args.size(); i++) {
                StringBuilder builder = new StringBuilder();
                ArrayList<Integer> tempList = new ArrayList<>();
                for (int ii = i; ii < args.size(); ii++) {
                    builder.append(args.get(ii));
                    tempList.add(ii);
                    if (builder.toString().equals(swear)) {
                        for (int iii : tempList) {
                            if (iii == tempList.get(0)) {
                                args.set(iii, "*****");
                            } else {
                                args.set(iii, "null");
                            }
                        }
                    }
                }
            }
        }

        for (String s : args) {
            if (!s.equals("null")) {
                newMessage.append(s).append(" ");
            }
        }

        return newMessage.toString();
    }

    private static String phrases(String message, List<String> swearList) {
        for (String s : swearList) {
            if (PatternCollection.STRING_PATTERN.matcher(s).find()) {
                String swear = PatternCollection.STRING_PATTERN.matcher(s).replaceAll("");
                if (message.contains(swear)) {
                    StringBuilder censor = new StringBuilder();
                    for (int i = 0; i < swear.length(); i++) {
                        censor.append("*");
                    }

                    message = message.replaceAll(swear, censor.toString());
                }
            }
        }
        return message;
    }

    private static String joinedWords(String message, List<String> swearList) {
        StringBuilder newMessage = new StringBuilder();
        String[] args = message.split(" ");

        for (String arg : args) {

            StringBuilder censor = new StringBuilder();
            for (int i = 0; i < arg.length(); i++) {
                censor.append("*");
            }

            for (String s : swearList) {

                if (PatternCollection.EXACT_PATTERN.matcher(s).find()) {
                    String swear = PatternCollection.EXACT_PATTERN.matcher(s).replaceAll("");
                    if (arg.equals(swear)) {
                        arg = censor.toString();
                    }
                }

                if (!PatternCollection.STRING_PATTERN.matcher(s).find() && !PatternCollection.EXACT_PATTERN.matcher(s).find()) {
                    if (arg.toLowerCase().contains(s)) {
                        arg = censor.toString();
                    }
                }
            }
            newMessage.append(arg).append(" ");
        }
        return newMessage.toString();
    }

    public static AchievementHandler getAchievementHandler(String achievement) {
        for (AchievementHandler h : PrisBungeeHandler.getInstance().achievementHandlers) {
            if (Objects.equals(h.getName(), achievement)) {
                return h;
            }
        }
        return null;
    }
}
