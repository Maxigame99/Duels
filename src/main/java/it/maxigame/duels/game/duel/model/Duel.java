package it.maxigame.duels.game.duel.model;

import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.duel.DuelStatus;
import it.maxigame.duels.game.duel.InventoryStorage;
import it.maxigame.duels.game.kit.KitHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Duel {

    private final KitHolder kit;
    private Arena arena;
    private DuelStatus status = DuelStatus.REQUESTED;

    private final Dueller requester;
    private final Dueller receiver;

    public Duel(Player requester, Player receiver, KitHolder kit) {
        this.requester = new Dueller(requester);
        this.receiver = new Dueller(receiver);
        this.kit = kit;
    }

}
