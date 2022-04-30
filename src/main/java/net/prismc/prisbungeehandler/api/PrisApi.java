package net.prismc.prisbungeehandler.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.util.UUID;

public class PrisApi {

    public static void achievementCheckAndExecute(PrisPlayer p) {
        if (PrisBungeeHandler.getInstance().pendingAchievementChecks.contains(p.getUniqueId()))
            return;

        PrisBungeeHandler.getInstance().pendingAchievementChecks.add(p.getUniqueId());
        ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
            for (AchievementHandler achievementHandler : PrisBungeeHandler.getInstance().achievementHandlers)
                achievementHandler.checkAndExecute(p);
            PrisBungeeHandler.getInstance().pendingAchievementChecks.remove(p.getUniqueId());
        });
    }

    public static void achievementCheckAndExecute(OfflinePrisPlayer p) {
        ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
            if (PrisBungeeHandler.getInstance().pendingAchievementChecks.contains(p.getUniqueId()))
                return;

            PrisApi api = new PrisApi();
            PrisBungeeHandler.getInstance().pendingAchievementChecks.add(p.getUniqueId());
            ProxyServer.getInstance().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> {
                for (AchievementHandler achievementHandler : PrisBungeeHandler.getInstance().achievementHandlers)
                    achievementHandler.checkAndExecute(api.wrapPlayer(ProxyServer.getInstance().getPlayer(p.getUniqueId())));
                PrisBungeeHandler.getInstance().pendingAchievementChecks.remove(p.getUniqueId());
            });
        });
    }

    public PrisPlayer wrapPlayer(ProxiedPlayer player) {
        if (PrisBungeeHandler.getInstance().cache.onlinePlayers.containsKey(player.getUniqueId())) {
            return PrisBungeeHandler.getInstance().cache.onlinePlayers.get(player.getUniqueId());
        }
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.GREEN + "New PrisPlayer.");
        return new PrisPlayer(player);
    }

    public OfflinePrisPlayer wrapOfflinePlayer(UUID uuid) {
        for (OfflinePrisPlayer p : PrisBungeeHandler.getInstance().cache.offlinePlayers) {
            if (p.getUniqueId().toString().equals(uuid.toString())) {
                return p;
            }
        }
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.YELLOW + "New UUID OfflinePlayer.");
        return new OfflinePrisPlayer(uuid);
    }

    public OfflinePrisPlayer wrapOfflinePlayer(int id) {
        for (OfflinePrisPlayer p : PrisBungeeHandler.getInstance().cache.offlinePlayers) {
            if (p.getID() == id) {
                return p;
            }
        }
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.YELLOW + "New ID OfflinePlayer.");
        return new OfflinePrisPlayer(id);
    }

    public OfflinePrisPlayer wrapOfflinePlayer(String username) {
        for (OfflinePrisPlayer p : PrisBungeeHandler.getInstance().cache.offlinePlayers) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                return p;
            }
        }
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.YELLOW + "New USERNAME OfflinePlayer.");
        return new OfflinePrisPlayer(username);
    }
}
