package it.maxigame.duels.game.duel;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
public class InventoryStorage {
    private ItemStack[] armorStorage;
    private ItemStack[] extraStorage;

    private int food = 0;
    private double health = 0;
    private int experience = 0;

    private int slot = 0;

    public InventoryStorage(Player player) {
        PlayerInventory inv = player.getInventory();
        armorStorage = inv.getArmorContents();
        extraStorage = inv.getExtraContents();
        food = player.getFoodLevel();
        health = player.getHealth();
        experience = player.getTotalExperience();
        slot = inv.getHeldItemSlot();
    }

    public static void assignData(InventoryStorage storage, Player player) {
        ItemStack[] armorStorage = storage.getArmorStorage();
        ItemStack[] extraStorage = storage.getExtraStorage();
        double health = storage.getHealth();
        int food = storage.getFood();
        int experience = storage.getExperience();
        int slot = storage.getSlot();

        PlayerInventory inv = player.getInventory();
        inv.setArmorContents(armorStorage);
        inv.setExtraContents(extraStorage);
        inv.setHeldItemSlot(slot);
        player.updateInventory();

        player.setHealth(health);
        player.setFoodLevel(food);
        player.setTotalExperience(experience);
    }
}
