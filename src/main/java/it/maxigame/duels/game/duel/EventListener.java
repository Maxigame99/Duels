package it.maxigame.duels.game.duel;

import it.maxigame.duels.Duels;
import it.maxigame.duels.api.DuelEndEvent;
import it.maxigame.duels.api.DuelStartEvent;
import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.arena.ArenaAgent;
import it.maxigame.duels.game.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (DuelManager.isDueling(event.getPlayer()))
            event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        Player death = event.getEntity();
        if (DuelManager.isDueling(death)) {
            Duel duel = DuelManager.getDuel(death);
            Bukkit.getPluginManager().callEvent(new DuelEndEvent(duel, DuelEndEvent.EndReason.DEATH));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDuelStartEvent(DuelStartEvent event) {
        Duel duel = event.getDuel();
        Player requester = duel.getRequester();
        Player receiver = duel.getReceiver();

        // calculate the arena
        Arena arena = ArenaAgent.calculareArena();
        if (arena==null) {
            requester.sendMessage("§cNon ci sono arene disponibili!");
            receiver.sendMessage("§cNon ci sono arene disponibili!");
            return;
        }
        duel.setArena(arena);

        requester.sendMessage("§e" + receiver.getName() + " §aha accettato il duello!");
        // change inventories
        duel.setRequesterInventory(requester.getInventory());
        duel.setReceiverInventory(receiver.getInventory());
        KitManager.assignKit(duel.getKit(), requester, receiver);

        // teleport players to arena
        requester.teleport(arena.getSpawn1());
        receiver.teleport(arena.getSpawn2());

        AtomicInteger counter = new AtomicInteger(3);
        Bukkit.getScheduler().runTaskTimerAsynchronously(Duels.getInstance(), (task) -> {
            if (counter.get() == 0) {
                duel.setStatus(DuelStatus.DUELING);
                requester.sendTitle("", "", 0, 1, 0);
                receiver.sendTitle("", "", 0, 1, 0);
                requester.playSound(requester.getLocation(), Sound.ENTITY_VILLAGER_YES, 2L, 2L);
                receiver.playSound(receiver.getLocation(), Sound.ENTITY_VILLAGER_YES, 2L, 2L);
                task.cancel();
                return;
            }

            requester.sendTitle("§c§l"+counter.get(), "", 0, 100, 0);
            receiver.sendTitle("§c§l"+counter.get(), "", 0, 100, 0);

            requester.playSound(requester.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2L, 2L);
            receiver.playSound(receiver.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2L, 2L);
            counter.decrementAndGet();
        }, 10L, 20L);
    }
}
