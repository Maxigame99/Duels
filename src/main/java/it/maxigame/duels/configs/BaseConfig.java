package it.maxigame.duels.configs;

import it.maxigame.duels.Duels;
import org.bukkit.configuration.file.FileConfiguration;

public enum BaseConfig {

    REWARDS("NONE");

    private static final FileConfiguration config = Duels.getConfigYaml();
    String dir;

    BaseConfig(String dir) {
        this.dir = dir;
    }

    public String getString() {
        return config.getString(dir);
    }
    public int getInt() {
        return config.getInt(dir);
    }
}
