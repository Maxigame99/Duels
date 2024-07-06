package it.maxigame.duels.api;

import it.maxigame.duels.game.duel.model.Duel;
import it.maxigame.duels.game.duel.model.Dueller;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DuelEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Duel duel;
    private final Dueller winner;
    private final Dueller loser;
    private final EndReason reason;

    public DuelEndEvent(Duel duel, Dueller winner, Dueller loser, EndReason reason) {
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
