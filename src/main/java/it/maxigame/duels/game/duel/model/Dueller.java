package it.maxigame.duels.game.duel.model;

import it.maxigame.duels.game.duel.InventoryStorage;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public class Dueller {

    private Player player;
    private InventoryStorage inventoryStorage;
    private Location location;
    private int food = 0;
    private double health = 0;
    private int experience = 0;


    public Dueller(Player player) {
        this.player = player;
        this.inventoryStorage = new InventoryStorage(player);
        this.location = player.getLocation();
        this.food = player.getFoodLevel();
        this.health = player.getHealth();
        this.experience = player.getTotalExperience();
    }


    public void assignData() {
        player.setHealth(health);
        player.setFoodLevel(food);
        player.setTotalExperience(experience);
        inventoryStorage.assignData(player);
    }
}
