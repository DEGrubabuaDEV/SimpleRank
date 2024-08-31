package de.grubabua.simplerank;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final File configFile;
    private FileConfiguration config;

    public ConfigManager(File configFile) {
        this.configFile = configFile;
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            config = new YamlConfiguration();
            saveConfig();
            return;
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
