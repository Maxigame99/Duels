package it.maxigame.duels.api;

import it.maxigame.duels.game.duel.Duel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DuelRefuseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Duel duel;
    private final RefuseCause cause;

    public DuelRefuseEvent(Duel duel, RefuseCause cause) {
        this.duel = duel;
        this.cause = cause;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }



    public enum RefuseCause {
        REJECTED, TIMEOUT
    }
}
