package it.maxigame.duels.game.duel;

import it.maxigame.duels.Duels;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class DuelManager {
    private final static ArrayList<Duel> duels = new ArrayList<>();
    private final static HashMap<Duel, BukkitTask> duelTasks = new HashMap<>();

    private final static ArrayList<Player> playersDueling = new ArrayList<>();

    public static Duel getDuel(Player requester, Player receiver) {
        for (Duel duel : duels)
            if (duel.getRequester()==requester && duel.getReceiver() == receiver)
                return duel;
        return null;
    }

    public static boolean requestDuel(Duel duel) {
        Player requester = duel.getRequester();
        Player receiver = duel.getReceiver();
        if (playersDueling.contains(requester) || playersDueling.contains(receiver))
            return false;

        duels.add(duel);

        // Start task for the request auto cancellation
        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(Duels.getInstance(), ()->{
            refuseDuel(duel);
        }, 1200L); //-> 1200 = 1 minute
        duelTasks.put(duel, task);
        return true;
    }


    public static void refuseDuel(Duel duel) {
        Player requester = duel.getRequester();
        Player receiver = duel.getReceiver();

        duels.remove(duel);
        duelTasks.remove(duel);


    }
}
