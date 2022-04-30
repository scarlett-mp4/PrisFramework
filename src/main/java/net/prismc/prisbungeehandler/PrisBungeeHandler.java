package net.prismc.prisbungeehandler;

import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.prismc.prisbungeehandler.achievements.achievements.*;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.commands.friend.Friends;
import net.prismc.prisbungeehandler.commands.friend.ListAlias;
import net.prismc.prisbungeehandler.commands.language.Language;
import net.prismc.prisbungeehandler.commands.levels.Levels;
import net.prismc.prisbungeehandler.commands.message.Message;
import net.prismc.prisbungeehandler.commands.message.Reply;
import net.prismc.prisbungeehandler.commands.party.Party;
import net.prismc.prisbungeehandler.commands.party.PartyListAlias;
import net.prismc.prisbungeehandler.commands.prisdebug.PrisDebug;
import net.prismc.prisbungeehandler.commands.settings.Settings;
import net.prismc.prisbungeehandler.communication.spigot.PrisPluginMessage;
import net.prismc.prisbungeehandler.communication.sql.Database;
import net.prismc.prisbungeehandler.communication.sql.PrisSQL;
import net.prismc.prisbungeehandler.files.configs.Config;
import net.prismc.prisbungeehandler.files.loader.*;
import net.prismc.prisbungeehandler.listeners.AchievementListener;
import net.prismc.prisbungeehandler.listeners.JoinListener;
import net.prismc.prisbungeehandler.listeners.QuitListener;
import net.prismc.prisbungeehandler.parties.PartyListeners;
import net.prismc.prisbungeehandler.utils.LocalPlayerCache;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  ______   ______     __     ______     __    __     ______
 * /\  == \ /\  == \   /\ \   /\  ___\   /\ "-./  \   /\  ___\
 * \ \  _-/ \ \  __<   \ \ \  \ \___  \  \ \ \-./\ \  \ \ \____
 * \ \_\    \ \_\ \_\  \ \_\  \/\_____\  \ \_\ \ \_\  \ \_____\
 * \/_/     \/_/ /_/   \/_/   \/_____/   \/_/  \/_/   \/_____/
 * <p>
 * PrisBungeeHandler Plugin
 * Handles the handling of the handler :)
 *
 * @author Skarless
 * @version Dev.2.0
 */

public final class PrisBungeeHandler extends Plugin {

    // Messaging Channel - Must Register In Startup
    public static final String BUNGEE_CHANNEL = "prismc:network";
    // Returns a static instance of GSON
    private static final Gson gson = new Gson();
    // The Database
    public static Database database;
    // Returns a static instance of PrisBungeeHandler
    private static PrisBungeeHandler instance;
    // Achievement Cache
    public final List<AchievementHandler> achievementHandlers = new ArrayList<>();
    public final List<UUID> pendingAchievementChecks = new ArrayList<>();
    // The Cache
    public LocalPlayerCache cache;
    // Configs
    public Config config = new Config("/PrisBungeeHandler/config.yml");
    public Config engConfig = new Config("/PrisBungeeHandler/language/english.yml");
    public Config spaConfig = new Config("/PrisBungeeHandler/language/spanish.yml");
    public Config freConfig = new Config("/PrisBungeeHandler/language/french.yml");
    public Config gerConfig = new Config("/PrisBungeeHandler/language/german.yml");
    public File languagePath = new File(ProxyServer.getInstance().getPluginsFolder() + "/PrisBungeeHandler/language");
    public File cachePath = new File(ProxyServer.getInstance().getPluginsFolder() + "/PrisBungeeHandler/cache");

    public static PrisBungeeHandler getInstance() {
        return instance;
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable() {
        instance = this;
        cache = new LocalPlayerCache();
        getProxy().registerChannel(BUNGEE_CHANNEL);

        try {
            initConfigs();
            database = new Database(this);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        initCommands();
        initListeners();
        initDatabase();
        initAchievements();

        getProxy().getConsole().sendMessage(new TextComponent(ChatColor.LIGHT_PURPLE + "[PrisBungeeHandler] " + ChatColor.GREEN + "has been enabled!"));
    }

    /**
     * Plugin shutdown logic
     */
    @Override
    public void onDisable() {
        try {
            Database.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Initialize listeners
     */
    public void initListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new QuitListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PartyListeners());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PrisPluginMessage());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AchievementListener());
    }

    /**
     * Initializes the MySQL Database
     */
    public void initDatabase() {
        PrisSQL prisSQL = new PrisSQL();
        try {
            prisSQL.importDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Initialize configs
     */
    public void initConfigs() {
        config.initConfig();
        spaConfig.initConfig();
        engConfig.initConfig();
        freConfig.initConfig();
        gerConfig.initConfig();

        if(config.isEmpty()) new ConfigLoader(config).loadMessages();
        if(spaConfig.isEmpty()) new SpaLoader(spaConfig).loadMessages();
        if(engConfig.isEmpty()) new EngLoader(engConfig).loadMessages();
        if(freConfig.isEmpty()) new FreLoader(freConfig).loadMessages();
        if(gerConfig.isEmpty()) new GerLoader(gerConfig).loadMessages();
    }

    /**
     * Initialize commands
     */
    public void initCommands() {
        getProxy().getPluginManager().registerCommand(this, new Friends("friend", "", "f", "friends", "amigos", "amigo", "amigas", "amiga"));
        getProxy().getPluginManager().registerCommand(this, new ListAlias("fl"));
        getProxy().getPluginManager().registerCommand(this, new PrisDebug("prisdebug"));
        getProxy().getPluginManager().registerCommand(this, new Settings("settings", ""));
        getProxy().getPluginManager().registerCommand(this, new Language("language"));
        getProxy().getPluginManager().registerCommand(this, new Party("party", "", "p"));
        getProxy().getPluginManager().registerCommand(this, new Message("message", "", "msg", "w", "whisper", "tell"));
        getProxy().getPluginManager().registerCommand(this, new PartyListAlias("pl"));
        getProxy().getPluginManager().registerCommand(this, new Reply("reply", "", "r"));
        getProxy().getPluginManager().registerCommand(this, new Levels("levels", "prismc.admin", "level", "lvls", "lvl", "exp", "xp", "prislevel"));
    }

    /**
     * Initialize achievements
     */
    public void initAchievements() {
        new Achieve_FirstLogin();
        new Achieve_FifteenFriend();
        new Achieve_FirstParty();
        new Achieve_OneFriend();
        new Achieve_OneHour();
        new Achieve_FiveHour();
        new Achieve_TwentyFourHour();
        new Achieve_TenMinute();
    }
}
