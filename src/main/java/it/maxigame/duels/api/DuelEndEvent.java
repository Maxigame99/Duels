package it.maxigame.duels.api;

import it.maxigame.duels.game.duel.Duel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DuelEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Duel duel;
    private final Player winner;
    private final Player loser;
    private final EndReason reason;

    public DuelEndEvent(Duel duel, Player winner, Player loser, EndReason reason) {
        this.duel = duel;
        this.winner = winner;
        this.loser = loser;
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum EndReason {
        DEATH, DISCONNECTED
    }
}
