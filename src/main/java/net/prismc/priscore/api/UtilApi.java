package net.prismc.priscore.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.luckperms.api.LuckPerms;
import net.prismc.priscore.PrisCore;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.ChatFilterLevels;
import net.prismc.priscore.utils.DefaultFontInfo;
import net.prismc.priscore.utils.LevelObject;
import net.prismc.priscore.utils.PatternCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class UtilApi {

    private final static int CENTER_PX = 154;

    public static List<String> getStringList(FileConfiguration f, String path) {
        List<String> output = new ArrayList<>();
        for (String s : f.getStringList(path)) {
            output.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return output;
    }

    public static LevelObject getLevelObject(int level) {
        for (LevelObject object : LevelObject.values()) {
            if (object.getNumber() == level) {
                return object;
            }
        }
        return null;
    }

    public static String parseColor(String message) {
        final Pattern hexPattern = Pattern.compile("<#" + "([A-Fa-f0-9]{6})" + ">");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static String getString(FileConfiguration f, String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(f.getString(path)));
    }

    public static int getInt(FileConfiguration f, String path) {
        return f.getInt(path);
    }

    public static double getDouble(FileConfiguration f, String path) {
        return f.getDouble(path);
    }

    public static boolean getBoolean(FileConfiguration f, String path) {
        return f.getBoolean(path);
    }

    public static String parseSwears(String message, ChatFilterLevels level) {
        List<String> swearList = new ArrayList<>();
        switch (level) {
            case HIGH:
                swearList.addAll(PrisCore.getInstance().getConfig().getStringList("Filter." + "HIGH"));
            case MEDIUM:
                swearList.addAll(PrisCore.getInstance().getConfig().getStringList("Filter." + "MEDIUM"));
            case LOW:
                swearList.addAll(PrisCore.getInstance().getConfig().getStringList("Filter." + "LOW"));

        }

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

    public static String getLevelLogo(int level) {
        for (LevelObject object : LevelObject.values()) {
            if (object.getNumber() == level)
                return object.getLogo();
        }
        return "Error!";
    }

    public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        entries.sort(Map.Entry.comparingByValue());

        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Map.Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private static String urlBlock(String message) {
        StringBuilder builder = new StringBuilder();
        String[] args = message.split(" ");
        String urlRegex = "((http://|https://)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(/([a-zA-Z-_/.0-9#:?=&;,]*)?)?)";
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

        Collections.addAll(args, stringArgs);

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

    /**
     * Creates a custom skull item
     *
     * @param url    - Custom skull data string.
     * @param amount - The amount of skulls in the item stack.
     * @param name   - Name of the skull item.
     */
    public static ItemStack createSkullItem(String url, int amount, String name) {
        @SuppressWarnings("deprecation")
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, amount, (short) 3);

        if (url.isEmpty()) {
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            assert headMeta != null;
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e.printStackTrace();
        }

        headMeta.setDisplayName(name);
        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Creates a custom skull item
     *
     * @param url    - Custom skull data string.
     * @param amount - The amount of skulls in the item stack.
     * @param name   - Name of the skull item.
     * @param lore   - Lore of the skull item.
     */
    public static ItemStack createSkullItem(String url, int amount, String name, List<String> lore) {
        @SuppressWarnings("deprecation")
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, amount, (short) 3);

        if (url.isEmpty()) {
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            assert headMeta != null;
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e.printStackTrace();
        }

        headMeta.setDisplayName(name);
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Creates a custom skull item
     *
     * @param m    - Material type in item stack.
     * @param name - Name of the material.
     * @param lore - Lore of the material.
     */
    public static ItemStack createItem(Material m, String name, List<String> lore) {
        ItemStack stack = new ItemStack(m);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Creates a custom skull item
     *
     * @param m    - Material type in item stack.
     * @param name - Name of the material.
     */
    public static ItemStack createItem(Material m, String name) {
        ItemStack stack = new ItemStack(m);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Returns the current time and date
     */
    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static boolean getServerStatus(String ip, int port) {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 10);
            s.close();
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    public static ItemStack createPlayerHead(PrisBukkitPlayer player) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.YELLOW + player.getName());
        meta.setOwningPlayer(player.getPlayer());
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createPlayerHead(OfflinePlayer player, String name) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setOwningPlayer(player.getPlayer());
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createPlayerHead(OfflinePlayer player, String name, List<String> lore) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setOwningPlayer(player.getPlayer());

        stack.setItemMeta(meta);
        return stack;
    }

    public static String centerMessage(String message) {
        if (message == null || message.equals("")) return "";
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
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
        return sb + message;
    }

    public static LuckPerms getLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        assert provider != null;
        return provider.getProvider();
    }
}
