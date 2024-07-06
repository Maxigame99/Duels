package it.maxigame.duels.configs;

import it.maxigame.duels.Duels;
import it.maxigame.duels.util.LazyItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public enum BaseConfig {

    REWARDS("reward-item");

    private static final FileConfiguration config = Duels.getConfigYaml();
    String dir;

    BaseConfig(String dir) {
        this.dir = dir;
    }

    public ItemStack getItemStack() {
        Material mat = Material.matchMaterial(config.getString(dir + ".material", "STONE"));
        String name = config.getString(dir + ".name");
        List<String> lore = config.getStringList(dir + ".lore");
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        config.getStringList(dir + ".enchantments").stream()
                .forEach(s -> {
                    String[] args = s.split(":");
                    Enchantment enchantment = Enchantment.getByName(args[0]);
                    int level = Integer.parseInt(args[1]);
                    enchantments.put(enchantment, level);
                });
        List<ItemFlag> flags = config.getStringList(dir+".flags").stream().map(s->ItemFlag.valueOf(s.toUpperCase())).toList();

        return LazyItemBuilder.builder()
                .material(mat)
                .name(name)
                .lores(lore)
                .enchantments(enchantments)
                .flags(flags)
                .build();
    }
}
