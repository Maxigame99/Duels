package it.maxigame.duels.gui;

import it.maxigame.duels.game.duel.Duel;
import it.maxigame.duels.game.kit.KitHolder;
import it.maxigame.duels.game.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class KitSelectorInventory implements InventoryHolder {
    private final String TITLE = "Selectiona il kit...";

    private final ArrayList<KitHolder> kits = KitManager.getKits();
    private final int[] invSizes = {9, 18, 27, 36, 45, 54};
    private final Inventory inv;

    private final Consumer<KitHolder> onSelect;

    public KitSelectorInventory(Consumer<KitHolder> onSelect) {
        this.inv = Bukkit.createInventory(this, calcSize(), TITLE);
        this.onSelect = onSelect;
        registerItems();
    }

    private int calcSize() {
        int length = kits.size();
        int div = Math.min((length / 9)-1, 0);
        return invSizes[div];
    }

    private void registerItems() {
        inv.addItem(kits.stream().map(KitHolder::getDisplayItem).toArray(ItemStack[]::new));
    }


    public void onClick(Player p, int slot, ClickType type) {
        int kitLength = kits.size();
        if (kitLength>=slot)
            return;

        KitHolder kit = kits.get(slot);
        onSelect.accept(kit);
    }


    public void open(Player player) {
        player.openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
