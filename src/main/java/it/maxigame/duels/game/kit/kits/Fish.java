package it.maxigame.duels.game.kit.kits;

import it.maxigame.duels.game.kit.KitHolder;
import it.maxigame.duels.util.LazyItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Fish implements KitHolder {
    @Override
    public String getName() {
        return "Fish";
    }

    @Override
    public ItemStack getDisplayItem() {
        return LazyItemBuilder.builder()
                .material(Material.TROPICAL_FISH)
                .name("§b§lFish")
                .addLore("§7by "+getAuthor())
                .build();
    }

    @Override
    public String getAuthor() {
        return "Maxigame99";
    }

    @Override
    public ItemStack[] getArmorSet() {
        return new ItemStack[]{
                null,
                LazyItemBuilder.builder().material(Material.LEATHER_CHESTPLATE).build(),
                LazyItemBuilder.builder().material(Material.LEATHER_LEGGINGS).build(),
                null
        };
    }

    @Override
    public ItemStack[] getContent() {
        return new ItemStack[]{
                LazyItemBuilder.builder().material(Material.TROPICAL_FISH).build()
        };
    }
}
