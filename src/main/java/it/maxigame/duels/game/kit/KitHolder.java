package it.maxigame.duels.game.kit;

import org.bukkit.inventory.ItemStack;

public interface KitHolder {

    String getName();
    ItemStack getDisplayItem();
    String getAuthor();

    ItemStack[] getArmorSet();
    ItemStack[] getContent();
}
