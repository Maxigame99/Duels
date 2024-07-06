package it.maxigame.duels.game.duel;

import it.maxigame.duels.Duels;
import it.maxigame.duels.api.DuelEndEvent;
import it.maxigame.duels.api.DuelStartEvent;
import it.maxigame.duels.configs.BaseConfig;
import it.maxigame.duels.data.cache.CacheHandler;
import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.arena.ArenaAgent;
import it.maxigame.duels.game.arena.ArenaStatus;
import it.maxigame.duels.game.duel.model.Duel;
import it.maxigame.duels.game.duel.model.Dueller;
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
        Dueller loser = DuelManager.getDueller(event.getEntity());
        if (loser!=null) {
            Duel duel = DuelManager.getDuel(loser.getPlayer());
            Dueller winner = duel.getReceiver()==loser ? duel.getRequester() : duel.getReceiver();
            Bukkit.getPluginManager().callEvent(new DuelEndEvent(duel, winner, loser, DuelEndEvent.EndReason.DEATH));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Dueller loser = DuelManager.getDueller(event.getPlayer());
        if (loser!=null) {
            Duel duel = DuelManager.getDuel(loser.getPlayer());
            Dueller winner = duel.getReceiver()==loser ? duel.getRequester() : duel.getReceiver();
            Bukkit.getPluginManager().callEvent(new DuelEndEvent(duel, winner, loser, DuelEndEvent.EndReason.DISCONNECTED));
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onDuelStartEvent(DuelStartEvent event) {
        Duel duel = event.getDuel();
        Arena arena = duel.getArena();
        Dueller requester = duel.getRequester();
        Player requesterPlayer = requester.getPlayer();

        Dueller receiver = duel.getReceiver();
        Player receiverPlayer = receiver.getPlayer();

        arena.setStatus(ArenaStatus.BATTLING);
        requesterPlayer.sendMessage("§e" + receiverPlayer.getName() + " §aha accettato il duello!");
        // change inventories
        KitManager.assignKit(duel.getKit(), requesterPlayer, receiverPlayer);

        // teleport players to arena
        requesterPlayer.teleport(arena.getSpawn1(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        receiverPlayer.teleport(arena.getSpawn2(), PlayerTeleportEvent.TeleportCause.PLUGIN);

        AtomicInteger counter = new AtomicInteger(3);
        Bukkit.getScheduler().runTaskTimerAsynchronously(Duels.getInstance(), (task) -> {
            if (counter.get() == 0) {
                duel.setStatus(DuelStatus.DUELING);
                requesterPlayer.sendTitle("", "", 0, 1, 0);
                receiverPlayer.sendTitle("", "", 0, 1, 0);
                requesterPlayer.playSound(requester.getLocation(), Sound.ENTITY_VILLAGER_YES, 2L, 2L);
                receiverPlayer.playSound(receiver.getLocation(), Sound.ENTITY_VILLAGER_YES, 2L, 2L);
                task.cancel();
                return;
            }

            requesterPlayer.sendTitle("§c§l" + counter.get(), "", 0, 100, 0);
            receiverPlayer.sendTitle("§c§l" + counter.get(), "", 0, 100, 0);

            requesterPlayer.playSound(requester.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2L, 2L);
            receiverPlayer.playSound(receiver.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2L, 2L);
            counter.decrementAndGet();
        }, 10L, 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDuelEndEvent(DuelEndEvent event) {
        Dueller winner = event.getWinner();
        Player winnerPlayer = winner.getPlayer();

        Dueller loser = event.getLoser();
        Player loserPlayer = loser.getPlayer();

        Duel duel = event.getDuel();
        DuelManager.cancelDuel(duel);
        duel.setStatus(DuelStatus.ENDED);

        winner.assignData();
        loser.assignData();

        winnerPlayer.sendMessage("§aHai vinto lo scontro, COMPLIMENTI!");
        winnerPlayer.playSound(loser.getLocation(), Sound.ENTITY_VILLAGER_YES, 2, 2);
        winnerPlayer.getInventory().addItem(BaseConfig.REWARDS.getItemStack());

        loserPlayer.sendMessage("§4Hai perso lo scontro :(");
        loserPlayer.playSound(loser.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 2, 2);

        cache.addWin(winnerPlayer.getUniqueId());
        cache.addLoses(loserPlayer.getUniqueId());

        ArenaAgent.clearArena(duel.getArena());

    }
}
