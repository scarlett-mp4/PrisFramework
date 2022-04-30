package net.prismc.prishub;

import net.prismc.prishub.commands.LobbyAnimate;
import net.prismc.prishub.commands.PrisHubCommand;
import net.prismc.prishub.listeners.JoinListener;
import net.prismc.prishub.lobby.LobbyFunction;
import net.prismc.prishub.lobby.LobbyLeaves;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PrisHub extends JavaPlugin {

    // Returns an instance of the plugin
    private static PrisHub instance;
    // Spawn Points
    public final List<Location> spawnPointsList = new ArrayList<>();
    // LobbyFunction Access
    public LobbyFunction lobbyFunction;

    public static PrisHub getInstance() {
        return instance;
    }


    /**
     * This method will be executed when the plugin is enabled
     * <p>
     * Contains methods that will initialize
     * the plugin. See bellow:
     */
    @Override
    public void onEnable() {
        instance = this;
        lobbyFunction = new LobbyFunction();

        initConfig();
        initCommands();
        initListeners();
        initSpawnPoints();
        new LobbyLeaves().initLeaves();

        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[PrisHub] " + ChatColor.GREEN + "has been enabled!");
    }

    // Initializes the configuration.
    public void initConfig() {
        saveDefaultConfig();
    }

    // Initializes events.
    public void initListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(lobbyFunction, this);
    }

    // Initializes commands.
    public void initCommands() {
        getCommand("prishub").setExecutor(new PrisHubCommand());
        getCommand("lobbyanimate").setExecutor(new LobbyAnimate());
    }

    // Stores spawn points in memory.
    public void initSpawnPoints() {
        List<String> pointList = instance.getConfig().getStringList("SpawnPoints");

        for (String s : pointList) {
            String[] points = s.split(" : ");
            Location loc = new Location(instance.getServer().getWorld(points[0]), Double.parseDouble(points[1]),
                    Double.parseDouble(points[2]), Double.parseDouble(points[3]), Float.parseFloat(points[4]), Float.parseFloat(points[5]));
            spawnPointsList.add(loc);
        }
    }
}
