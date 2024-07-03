package it.maxigame.duels.game.kit;

import org.bukkit.inventory.PlayerInventory;

public interface KitHolder {

    String getName();
    String getDisplayItem();
    String getAuthor();

    PlayerInventory getPlayerInventory();
}
