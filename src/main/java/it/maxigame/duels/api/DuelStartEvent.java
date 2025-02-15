package it.maxigame.duels.api;

import it.maxigame.duels.game.duel.model.Duel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DuelStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Duel duel;

    public DuelStartEvent(Duel duel) {
        this.duel = duel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
