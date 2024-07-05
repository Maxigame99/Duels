package it.maxigame.duels.gui;

import it.maxigame.duels.game.kit.KitHolder;
import it.maxigame.duels.game.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class KitSelectorInventory implements InventoryHolder {
    private final String TITLE = "Seleziona il kit...";

    private final List<KitHolder> kits;
    private final int[] invSizes = {9, 18, 27, 36, 45, 54};
    private final Inventory inv;

    private final Player player;
    private final Consumer<KitHolder> onSelect;

    public KitSelectorInventory(Player player, Consumer<KitHolder> onSelect) {
        this.player = player;
        this.onSelect = onSelect;
        this.kits = KitManager.getKits().stream().filter(k->player.hasPermission("duels.kit."+k.getName())).toList();
        this.inv = Bukkit.createInventory(this, calcSize(), TITLE);
        registerItems();
    }

    private int calcSize() {
        int length = kits.size();
        int div = (int) Math.max(Math.ceil((length / 9)-1), 0);
        return invSizes[div];
    }

    private void registerItems() {
        inv.addItem(kits.stream().map(KitHolder::getDisplayItem).toArray(ItemStack[]::new));
    }


    public void onClick(Player p, int slot, ClickType type) {
        int kitLength = kits.size();
        if (slot>=kitLength)
            return;

        KitHolder kit = kits.get(slot);
        p.closeInventory();
        onSelect.accept(kit);
    }


    public void open() {
        player.openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
