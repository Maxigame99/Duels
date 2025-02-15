package it.maxigame.duels.game.duel;

import it.maxigame.duels.Duels;
import it.maxigame.duels.api.DuelRefuseEvent;
import it.maxigame.duels.api.DuelStartEvent;
import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.arena.ArenaAgent;
import it.maxigame.duels.game.duel.model.Duel;
import it.maxigame.duels.game.duel.model.Dueller;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class DuelManager {
    private final static ArrayList<Duel> duels = new ArrayList<>();
    private final static HashMap<Duel, BukkitTask> duelTasks = new HashMap<>();

    private final static HashMap<Player, Duel> playersDueling = new HashMap<>();

    public static Duel getDuel(Player requester, Player receiver) {
        for (Duel duel : duels)
            if (duel.getRequester()==requester && duel.getReceiver() == receiver)
                return duel;
        return null;
    }
    public static Duel getDuel(Player duelingPlayer) {
        return playersDueling.get(duelingPlayer);
    }
    public static Dueller getDueller(Player player) {
        Duel duel = getDuel(player);
        Dueller dueller = null;
        if (duel.getReceiver().getPlayer() == player)
            dueller = duel.getReceiver();
        else if (duel.getRequester().getPlayer() == player)
            dueller = duel.getRequester();
        return dueller;
    }

    public static boolean isDueling(Player player) {
        return playersDueling.containsKey(player);
    }

    public static boolean requestDuel(Duel duel) {
        Player requester = duel.getRequester().getPlayer();
        Player receiver = duel.getReceiver().getPlayer();
        if (playersDueling.containsKey(requester) || playersDueling.containsKey(receiver))
            return false;

        duels.add(duel);

        // Start task for the request auto cancellation
        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(Duels.getInstance(), ()->{
            Bukkit.getScheduler().runTask(Duels.getInstance(), ()-> Bukkit.getPluginManager().callEvent(new DuelRefuseEvent(duel, DuelRefuseEvent.RefuseCause.TIMEOUT)));
            cancelDuel(duel);
        }, 1200L); //-> 1200 = 1 minute
        duelTasks.put(duel, task);
        return true;
    }

    public static void startDuel(Duel duel) {
        // calculate the arena
        Arena arena = ArenaAgent.calculareArena();
        Player requester = duel.getRequester().getPlayer();
        Player receiver = duel.getReceiver().getPlayer();
        if (arena == null) {
            requester.sendMessage("§cNon ci sono arene disponibili!");
            receiver.sendMessage("§cNon ci sono arene disponibili!");
            return;
        }
        duelTasks.remove(duel).cancel();
        playersDueling.put(requester, duel);
        playersDueling.put(receiver, duel);
        duel.setStatus(DuelStatus.STARTING);
        Bukkit.getPluginManager().callEvent(new DuelStartEvent(duel));
    }

    public static void cancelDuel(Duel duel) {
        playersDueling.remove(duel.getReceiver().getPlayer());
        playersDueling.remove(duel.getReceiver().getPlayer());
        duels.remove(duel);
        duelTasks.remove(duel).cancel();
    }
}
