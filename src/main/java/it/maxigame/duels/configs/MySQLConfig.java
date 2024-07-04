package it.maxigame.duels.configs;

import it.maxigame.duels.Duels;
import org.bukkit.configuration.file.FileConfiguration;

public enum MySQLConfig {

    ADDRESS("address"),
    PORT("port"),
    USERNAME("username"),
    PASSWORD("password"),

    DATABASE("database");

    private static final FileConfiguration config = Duels.getConfigYaml();
    final String dir;

    MySQLConfig(String dir) {
        this.dir = "MySQL."+dir;
    }


    public String getString() {
        return config.getString(dir);
    }
    public int getInt() {
        return config.getInt(dir);
    }
}
