package it.maxigame.duels.api;

import it.maxigame.duels.game.duel.Duel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DuelEndEvent extends Event {
    @Getter
    private static final HandlerList handlers = new HandlerList();

    private final Duel duel;
    private final EndReason reason;

    public DuelEndEvent(Duel duel, EndReason reason) {
        this.duel = duel;
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public enum EndReason {
        DEATH, DISCONNECTED
    }
}
