package it.maxigame.duels.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;

@Getter
@Setter
public class DataModel {
    private final OfflinePlayer player;
    private int wins;
    private int loses;

    public DataModel(OfflinePlayer player, int wins, int loses) {
        this.player = player;
        this.wins = wins;
        this.loses = loses;
    }
}
