package it.maxigame.duels.game.kit;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface KitHolder {

    String getName();
    ItemStack getDisplayItem();
    String getAuthor();

    @NotNull ItemStack[] getArmorSet();
    @NotNull ItemStack[] getContent();
}
