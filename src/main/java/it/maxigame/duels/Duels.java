package it.maxigame.duels;

import it.maxigame.duels.commands.ArenasCommand;
import it.maxigame.duels.commands.CreateArenaCommand;
import it.maxigame.duels.commands.DeleteArenaCommand;
import it.maxigame.duels.commands.DuelCommands;
import it.maxigame.duels.configs.MySQLConfig;
import it.maxigame.duels.data.CacheHandler;
import it.maxigame.duels.data.DataListener;
import it.maxigame.duels.data.MySQLConnector;
import it.maxigame.duels.game.arena.ArenaAgent;
import it.maxigame.duels.game.duel.EventListener;
import it.maxigame.duels.gui.InventoryListener;
import it.maxigame.duels.world.WorldCreator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Duels extends JavaPlugin {

    @Getter
    private static Duels instance;
    @Getter
    private static FileConfiguration configYaml;

    private CacheHandler cache;


    @Override
    public void onEnable() {
        instance = this;

        // Setup the configuration file
        saveDefaultConfig();
        configYaml = getConfig();

        // Load caches
        cache = new CacheHandler(new MySQLConnector(
                MySQLConfig.ADDRESS.getString(),
                MySQLConfig.PORT.getInt(),
                MySQLConfig.DATABASE.getString(),
                MySQLConfig.USERNAME.getString(),
                MySQLConfig.PASSWORD.getString()
        ));
        cache.loadCaches();

        // Command register
        getCommand("duel").setExecutor(new DuelCommands());
        getCommand("arenas").setExecutor(new ArenasCommand());
        getCommand("setarena").setExecutor(new CreateArenaCommand());
        getCommand("delarena").setExecutor(new DeleteArenaCommand());

        // Event Register
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new EventListener(cache), this);
        Bukkit.getPluginManager().registerEvents(new DataListener(cache), this);

        // Load all arenas from config.yml
        WorldCreator.worldInit();
        ArenaAgent.laodArenas();
    }

    public void onDisable() {
        if (cache!=null)
            cache.saveCaches();
        ArenaAgent.saveArenas();
    }

}
