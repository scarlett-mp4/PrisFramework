package net.prismc.priscore;

import com.google.gson.Gson;
import net.prismc.priscore.achievements.AchievementHandler;
import net.prismc.priscore.achievements.AchievementListener;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.commands.*;
import net.prismc.priscore.files.EngConfiguration;
import net.prismc.priscore.files.FreConfiguration;
import net.prismc.priscore.files.GerConfiguration;
import net.prismc.priscore.files.SpaConfiguration;
import net.prismc.priscore.listeners.*;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.utils.Expansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * ______   ______     __     ______     __    __     ______
 * /\  == \ /\  == \   /\ \   /\  ___\   /\ "-./  \   /\  ___\
 * \ \  _-/ \ \  __<   \ \ \  \ \___  \  \ \ \-./\ \  \ \ \____
 * \ \_\    \ \_\ \_\  \ \_\  \/\_____\  \ \_\ \ \_\  \ \_____\
 * \/_/     \/_/ /_/   \/_/   \/_____/   \/_/  \/_/   \/_____/
 * <p>
 * PrisCore Plugin
 *
 * @author Skarless
 * @author more soon...? :>
 * @version Dev.2.0
 */

public final class PrisCore extends JavaPlugin {

    // Messaging Channel; Must Be Registered!
    public static final String BUNGEE_CHANNEL = "prismc:network";
    // Gson
    public static final Gson gson = new Gson();
    // Returns an instance of PrisCore
    private static PrisCore instance;
    // PrisBukkitPlayer Cache
    public HashMap<UUID, PrisBukkitPlayer> cache = new HashMap<>();
    // Achievements
    public ProxyEventListener proxyEventListener;
    public AchievementHandler achievementHandler;
    // Config Files
    public EngConfiguration engConfiguration = new EngConfiguration();
    public SpaConfiguration spaConfiguration = new SpaConfiguration();
    public FreConfiguration freConfiguration = new FreConfiguration();
    public GerConfiguration gerConfiguration = new GerConfiguration();
    public File languagePath = new File(getDataFolder() + "/language");
    // Catcher
    public boolean isReady = false;

    public static PrisCore getInstance() {
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
        instance = (this);
        proxyEventListener = new ProxyEventListener(this, UtilApi.getLuckPerms());
        achievementHandler = new AchievementHandler();

        initConfigs();
        initCommands();
        initListeners();
        initServer();
        registerPlaceholders();
        registerMessagingChannel();

        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[PrisCore] " + ChatColor.GREEN + "has been enabled!");
    }

    /**
     * Loads the configuration files
     */
    public void initConfigs() {
        try {

            if (!this.getDataFolder().exists())
                this.getDataFolder().mkdirs();

            if (!this.languagePath.exists())
                this.languagePath.mkdirs();

            saveDefaultConfig();
            engConfiguration.saveDefaultConfig();
            spaConfiguration.saveDefaultConfig();
            freConfiguration.saveDefaultConfig();
            gerConfiguration.saveDefaultConfig();
            getLogger().info("[PrisCore] Configuration files have successfully loaded!");

        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("[PrisCore] Could not load configuration files!");
        }
    }

    /**
     * Loads the commands
     */
    public void initServer() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            isReady = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Pris Server Is Now Enabled!");
        }, 150L);
    }

    /**
     * Loads the commands
     */
    public void initCommands() {
        try {
            Objects.requireNonNull(getCommand("clear")).setExecutor(new Clear());
            Objects.requireNonNull(getCommand("enderchest")).setExecutor(new Enderchest());
            Objects.requireNonNull(getCommand("feed")).setExecutor(new Feed());
            Objects.requireNonNull(getCommand("fly")).setExecutor(new Fly());
            Objects.requireNonNull(getCommand("gamemode")).setExecutor(new Gamemode());
            Objects.requireNonNull(getCommand("god")).setExecutor(new God());
            Objects.requireNonNull(getCommand("hat")).setExecutor(new Hat());
            Objects.requireNonNull(getCommand("setwarp")).setExecutor(new Setwarp());
            Objects.requireNonNull(getCommand("delwarp")).setExecutor(new Delwarp());
            Objects.requireNonNull(getCommand("warp")).setExecutor(new Warp());
            Objects.requireNonNull(getCommand("heal")).setExecutor(new Heal());
            Objects.requireNonNull(getCommand("speed")).setExecutor(new Speed());
            Objects.requireNonNull(getCommand("health")).setExecutor(new Health());
            Objects.requireNonNull(getCommand("bukkitdebug")).setExecutor(new BukkitDebug());
            Objects.requireNonNull(getCommand("priscore")).setExecutor(new PrisCoreCommand());
            Objects.requireNonNull(getCommand("achievementtest")).setExecutor(new AchievementTest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the placeholders
     */
    public void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Expansion(this).register();
        }
    }

    /**
     * Loads the messaging channels
     */
    public void registerMessagingChannel() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, BUNGEE_CHANNEL, new MessageListener());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, BUNGEE_CHANNEL);
    }

    /**
     * Loads the listeners
     */
    public void initListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new AchievementListener(), this);
        getServer().getPluginManager().registerEvents(new LevelListener(), this);
        getServer().getPluginManager().registerEvents(proxyEventListener, this);
        getServer().getPluginManager().registerEvents(achievementHandler, this);
        proxyEventListener.register();
    }
}
