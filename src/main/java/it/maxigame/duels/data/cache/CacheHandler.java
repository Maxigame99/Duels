package it.maxigame.duels.data.cache;

import it.maxigame.duels.data.DataModel;
import it.maxigame.duels.data.MySQLConnector;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CacheHandler {
    private final HashMap<UUID, DataModel> duellers = new HashMap<>();
    private final MySQLConnector mysql;

    public CacheHandler(MySQLConnector mysql) {
        this.mysql = mysql;
    }

    public void addWin(UUID uuid) {
        DataModel dm = duellers.get(uuid);
        if (dm == null) return;
        int wins = dm.getWins();
        dm.setWins(wins+1);
    }
    public void addLoses(UUID uuid) {
        DataModel dm = duellers.get(uuid);
        if (dm == null) return;
        int loses = dm.getLoses();
        dm.setLoses(loses+1);
    }

    public void saveCache(UUID uuid) {
        DataModel dm = duellers.get(uuid);
        if (dm==null) return;
        String username = dm.getPlayer().getName();
        mysql.setWins(username, dm.getWins());
        mysql.setLoses(username, dm.getLoses());
    }
    public void loadCache(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String username = player.getName();
        int wins = mysql.getWins(username);
        int loses = mysql.getLoses(username);
        DataModel dm = new DataModel(player, wins, loses);
        duellers.put(uuid, dm);
    }
    public void saveCaches() {
        System.out.println("§eSaving data caches...");
        duellers.keySet().forEach(this::saveCache);
        System.out.println("§eDone!");
    }
    public void loadCaches() {
        System.out.println("§eLoading data caches...");
        Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).forEach(this::loadCache);
        System.out.println("§eDone!");
    }
}
