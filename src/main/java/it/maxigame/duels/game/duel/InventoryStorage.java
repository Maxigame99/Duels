package it.maxigame.duels.game.duel;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
public class InventoryStorage {
    private ItemStack[] armorStorage;
    private ItemStack[] extraStorage;

    private int slot = 0;

    public InventoryStorage(Player player) {
        PlayerInventory inv = player.getInventory();
        armorStorage = inv.getArmorContents();
        extraStorage = inv.getExtraContents();
        slot = inv.getHeldItemSlot();
    }

    public void assignData(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setArmorContents(armorStorage);
        inv.setExtraContents(extraStorage);
        inv.setHeldItemSlot(slot);
        player.updateInventory();
    }
}
