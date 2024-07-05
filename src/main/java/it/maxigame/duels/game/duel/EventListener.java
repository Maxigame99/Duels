package it.maxigame.duels.game.duel;

import it.maxigame.duels.Duels;
import it.maxigame.duels.api.DuelEndEvent;
import it.maxigame.duels.api.DuelStartEvent;
import it.maxigame.duels.data.cache.CacheHandler;
import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.arena.ArenaAgent;
import it.maxigame.duels.game.arena.ArenaStatus;
import it.maxigame.duels.game.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class EventListener implements Listener {

    private final CacheHandler cache;
    public EventListener(CacheHandler cache) {
        this.cache = cache;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (DuelManager.isDueling(event.getPlayer()))
            event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent event) {
        if (DuelManager.isDueling(event.getPlayer()) && event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN)
            event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if (DuelManager.isDueling(player))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        Player loser = event.getEntity();
        if (DuelManager.isDueling(loser)) {
            Duel duel = DuelManager.getDuel(loser);
            Player winner = duel.getReceiver()==loser ? duel.getRequester() : duel.getReceiver();
            Bukkit.getPluginManager().callEvent(new DuelEndEvent(duel, winner, loser, DuelEndEvent.EndReason.DEATH));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Player loser = event.getPlayer();
        if (DuelManager.isDueling(loser)) {
            Duel duel = DuelManager.getDuel(loser);
            Player winner = duel.getReceiver()==loser ? duel.getRequester() : duel.getReceiver();
            Bukkit.getPluginManager().callEvent(new DuelEndEvent(duel, winner, loser, DuelEndEvent.EndReason.DISCONNECTED));
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onDuelStartEvent(DuelStartEvent event) {
        Duel duel = event.getDuel();
        Arena arena = duel.getArena();
        Player requester = duel.getRequester();
        Player receiver = duel.getReceiver();

        arena.setStatus(ArenaStatus.BATTLING);
        requester.sendMessage("§e" + receiver.getName() + " §aha accettato il duello!");
        // change inventories
        duel.setRequesterStorage(new InventoryStorage(requester));
        duel.setReceiverStorage(new InventoryStorage(receiver));
        KitManager.assignKit(duel.getKit(), requester, receiver);

        // teleport players to arena
        requester.teleport(arena.getSpawn1(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        receiver.teleport(arena.getSpawn2(), PlayerTeleportEvent.TeleportCause.PLUGIN);

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

            requester.sendTitle("§c§l" + counter.get(), "", 0, 100, 0);
            receiver.sendTitle("§c§l" + counter.get(), "", 0, 100, 0);

            requester.playSound(requester.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2L, 2L);
            receiver.playSound(receiver.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2L, 2L);
            counter.decrementAndGet();
        }, 10L, 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDuelEndEvent(DuelEndEvent event) {
        Player winner = event.getWinner();
        Player loser = event.getLoser();
        Duel duel = event.getDuel();
        DuelManager.reassignInventories(duel);

        winner.sendMessage("§aHai vinto lo scontro, COMPLIMENTI!");
        winner.playSound(loser.getLocation(), Sound.ENTITY_VILLAGER_YES, 2, 2);

        loser.sendMessage("§4Hai perso lo scontro :(");
        loser.playSound(loser.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 2, 2);

        cache.addWin(winner.getUniqueId());
        cache.addLoses(loser.getUniqueId());

        ArenaAgent.clearArena(duel.getArena());
        duel.setStatus(DuelStatus.ENDED);
    }
}
