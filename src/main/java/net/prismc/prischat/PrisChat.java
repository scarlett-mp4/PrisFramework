package net.prismc.prischat;

import net.luckperms.api.LuckPerms;
import net.prismc.prischat.Files.FormatConfiguration;
import net.prismc.prischat.Listeners.AsyncChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisChat extends JavaPlugin {

    private static PrisChat instance;
    public FormatConfiguration formatConfiguration = new FormatConfiguration();

    public static PrisChat getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        loadConfiguration();
        loadListeners();

        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[PrisChat] " + ChatColor.GREEN + "has been enabled!");
    }

    /**
     * Loads the listeners
     */
    public void loadListeners() {
        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
    }

    /**
     * Loads the configuration files
     */
    public void loadConfiguration() {
        try {
            formatConfiguration.saveDefaultConfig();
            getLogger().info("[PrisChat] Configuration files have successfully loaded!");

        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("[PrisChat] Could not load configuration files!");
        }
    }

    /**
     * Returns an instance of LuckPerms
     */
    public LuckPerms getLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        return provider.getProvider();
    }
}
