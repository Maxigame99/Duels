package it.maxigame.duels;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Duels extends JavaPlugin {

    @Getter
    private static Duels instance;
    @Getter
    private static FileConfiguration config;


    @Override
    public void onEnable() {
        instance = this;

        // Setup the configuration file
        saveDefaultConfig();
        config = getConfig();
    }

    public void onDisable() {

    }

}
