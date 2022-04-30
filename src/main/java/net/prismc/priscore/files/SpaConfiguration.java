package net.prismc.priscore.files;

import net.prismc.priscore.PrisCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class SpaConfiguration {

    // Variables
    private final String Name = "language/spa.yml";
    private FileConfiguration Config = null;
    private File File = null;

    // Method to reload the config.
    public void reloadConfig() {
        if (this.File == null) {
            this.File = new File(PrisCore.getInstance().getDataFolder(), Name);
        }
        this.Config = YamlConfiguration.loadConfiguration(this.File);

        InputStream doorStream = PrisCore.getInstance().getResource(Name);
        if (doorStream != null) {
            YamlConfiguration defaultWarpConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(doorStream));
            this.Config.setDefaults(defaultWarpConfig);
        }
    }

    // Method to grab the config.
    public FileConfiguration getConfig() {
        if (this.Config == null) {
            reloadConfig();
        }
        return this.Config;
    }

    // Method to save the config file.
    public void saveConfig() {
        if (this.Config == null || this.File == null) {
            return;
        }
        try {
            this.getConfig().save(this.File);
        } catch (IOException e) {
            PrisCore.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + File, e);
        }
    }

    // Method to load the default config.
    // Plugin will grab from the set defaults if the user does not specify said value.
    public void saveDefaultConfig() {
        if (this.File == null) {
            this.File = new File(PrisCore.getInstance().getDataFolder(), Name);
        }
        if (!this.File.exists()) {
            PrisCore.getInstance().saveResource(Name, false);
        }
    }

}