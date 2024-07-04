package it.maxigame.duels;

import it.maxigame.duels.commands.ArenaCommand;
import it.maxigame.duels.commands.DuelCommands;
import it.maxigame.duels.game.arena.ArenaAgent;
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
        getCommand("duel").setExecutor(new DuelCommands());
        getCommand("arena").setExecutor(new ArenaCommand());
        saveDefaultConfig();
        config = getConfig();

        ArenaAgent.laodArenas();
    }

    public void onDisable() {
        ArenaAgent.saveArenas();
    }

}
