package net.prismc.prischat.Listeners;

import net.luckperms.api.LuckPerms;
import net.prismc.prischat.PrisChat;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.ChatFilterLevels;
import net.prismc.priscore.utils.PatternCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        PrisBukkitPlayer p = PrisCoreApi.wrapPrisBukkitPlayer(e.getPlayer());
        ConfigurationSection config = PrisChat.getInstance().formatConfiguration.getConfig();
        String playerGroup = PrisChat.getInstance().getLuckPerms().getUserManager().getUser(p.getUniqueId()).getPrimaryGroup();
        playerGroup = playerGroup.substring(0, 1).toUpperCase() + playerGroup.substring(1);
        Pattern pattern = Pattern.compile("^[A-Za-z0-9-~!@#$%^&*()<>_+=-{}|';:.,\\[\"\"]|';:.,/?><_.]+$");
        Matcher matcher = pattern.matcher(e.getMessage().toLowerCase().replaceAll("\\s+", ""));
        String format = "";

        e.setCancelled(true);

        if (!matcher.find()) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Chat.BannedCharacters"));
            return;
        }

        if (config.contains(playerGroup)) {
            format = PatternCollection.PLAYER_PATTERN.matcher(Objects.requireNonNull(config.getString(playerGroup))).replaceAll(p.getName());

        } else {
            format = PatternCollection.PLAYER_PATTERN.matcher(Objects.requireNonNull(config.getString("Default"))).replaceAll(p.getName());
        }

        format = UtilApi.parseColor(format);
        if (p.hasPermission("prismc.chatcolor")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        for (Player receiver : Bukkit.getOnlinePlayers()) {
            ChatFilterLevels setting = PrisCoreApi.wrapPrisBukkitPlayer(receiver).getFilterLevel();
            String message = "";

            switch (setting) {
                case HIGH:
                    message = UtilApi.parseSwears(e.getMessage(), ChatFilterLevels.HIGH);
                    break;
                case MEDIUM:
                    message = UtilApi.parseSwears(e.getMessage(), ChatFilterLevels.MEDIUM);
                    break;
                case LOW:
                    message = UtilApi.parseSwears(e.getMessage(), ChatFilterLevels.LOW);
                    break;
            }

            String finalMessage = PatternCollection.LEVEL_PATTERN.matcher(format + message).replaceAll(UtilApi.getLevelLogo(p.getPrisLevel()));
            receiver.sendMessage(finalMessage);
        }
    }
}
