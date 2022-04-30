package net.prismc.prisbungeehandler.files.configs;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.prismc.prisbungeehandler.PrisBungeeHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Config {
    private final String path;
    private Configuration config;
    private File file;

    public Config(String path) {
        this.path = path;
    }

    public boolean isEmpty() {
        try {
            Scanner scan = new Scanner(file);
            if(!scan.hasNext())
                return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Initializes the config
     */
    public void initConfig() {
        file = new File(ProxyServer.getInstance().getPluginsFolder() + path);

        try {
            if (!PrisBungeeHandler.getInstance().getDataFolder().exists()) {
                PrisBungeeHandler.getInstance().getDataFolder().mkdirs();
            }
            if (!PrisBungeeHandler.getInstance().languagePath.exists()) {
                PrisBungeeHandler.getInstance().languagePath.mkdirs();
            }
            if (!PrisBungeeHandler.getInstance().cachePath.exists()) {
                PrisBungeeHandler.getInstance().cachePath.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                return;
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the config
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * Saves the config
     */
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the config
     */
    public void reloadConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
