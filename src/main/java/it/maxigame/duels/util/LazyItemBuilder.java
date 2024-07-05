package it.maxigame.duels.util;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Accessors(fluent = true)
@Setter
public class LazyItemBuilder {
    private Material material = Material.STONE;
    private int amount = 1;
    private String name;
    private Color color;
    private List<String> lores = new ArrayList<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private boolean unbreakable = false;

    public LazyItemBuilder addFlag(ItemFlag flag) {
        flags.add(flag);
        return this;
    }
    public LazyItemBuilder addEnchantment(Enchantment enc, int level) {
        this.enchantments.put(enc, level);
        return this;
    }
    public LazyItemBuilder addLore(String line) {
        this.lores.add(line);
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setLore(lores.stream().map(s->ChatColor.translateAlternateColorCodes('&', s)).toList());
        meta.addItemFlags(flags.toArray(ItemFlag[]::new));
        enchantments.forEach((key, value) -> item.addEnchantment(key, value));
        meta.setUnbreakable(unbreakable);

        item.setItemMeta(meta);
        return item;
    }

    public static Color getColorByString(String color) {
        if (color==null) color="0;0;0";
        String[] s = color.split(";");
        return Color.fromBGR(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
    }

    public static LazyItemBuilder builder() {
        return new LazyItemBuilder();
    }
}
