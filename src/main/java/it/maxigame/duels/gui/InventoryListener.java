package it.maxigame.duels.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof KitSelectorInventory) {
            e.setCancelled(true);

            KitSelectorInventory inv = (KitSelectorInventory) holder;

            Player clicker = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            ClickType type = e.getClick();
            inv.onClick(clicker, slot, type);
        }
    }
}
