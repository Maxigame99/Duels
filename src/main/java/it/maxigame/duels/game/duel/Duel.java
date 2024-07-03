package it.maxigame.duels.game.duel;

import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.kit.KitHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
public class Duel {

    private KitHolder kit;
    @Setter
    private Arena arena;
    private DuelStatus status = DuelStatus.REQUESTED;

    private final Player requester;
    private final Player receiver;

    public Duel(Player requester, Player receiver, KitHolder kit) {
        this.requester = requester;
        this.receiver = receiver;
        this.kit = kit;
    }

}
