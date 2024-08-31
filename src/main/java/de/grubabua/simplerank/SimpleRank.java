package de.grubabua.simplerank;


import de.grubabua.simplerank.commands.*;
import de.grubabua.simplerank.namechange.NameChangeEvents;
import de.grubabua.simplerank.permission.PermissionEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SimpleRank extends JavaPlugin {
    private ConfigManager configManager;
    private File configFile;


    public static FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        configFile = new File(getDataFolder(), "config.yml");
        configManager = new ConfigManager(configFile);
        configManager.loadConfig();

        getCommand("rank").setExecutor(new rank(this));
        getCommand("rank").setTabCompleter(new  de.grubabua.simplerank.tabcompleter.rank(this));
        getCommand("currentrank").setExecutor(new currentrank(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));

        getServer().getPluginManager().registerEvents(new NameChangeEvents(this), this);
        getServer().getPluginManager().registerEvents(new PermissionEvents(this), (this));
    }

    @Override
    public void onDisable() {
        configManager.saveConfig();
        saveConfig();
    }
}
