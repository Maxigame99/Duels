package it.maxigame.duels.game.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

public class KitManager {

    private static HashMap<String, KitHolder> kits = new HashMap<>();


    public static ArrayList<KitHolder> getKits() {
        return new ArrayList<>(kits.values());
    }

    public static boolean register(KitHolder kit) {
        String name = kit.getName();
        if (kits.containsKey(name))
            return false;
        kits.put(name, kit);
        return true;
    }

    public static void unregister(String name) {
        kits.remove(name);
    }

    public static void assignKit(KitHolder kit, Player... players) {
        ItemStack[] armor = kit.getArmorSet();
        ItemStack[] content = kit.getContent();
        for (Player player : players) {
            PlayerInventory inv = player.getInventory();
            inv.setArmorContents(armor);
            inv.setContents(content);
            inv.setHeldItemSlot(0);
            player.updateInventory();

            player.setTotalExperience(0);
            player.setHealth(20);
            player.setFoodLevel(20);
        }
    }
}
