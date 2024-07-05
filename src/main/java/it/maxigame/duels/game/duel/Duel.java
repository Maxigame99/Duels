package it.maxigame.duels.game.duel;

import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.kit.KitHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@Getter
@Setter
public class Duel {

    private final KitHolder kit;
    private Arena arena;
    private DuelStatus status = DuelStatus.REQUESTED;

    private final Player requester;
    private InventoryStorage requesterStorage;
    private final Player receiver;
    private InventoryStorage receiverStorage;

    public Duel(Player requester, Player receiver, KitHolder kit) {
        this.requester = requester;
        this.receiver = receiver;
        this.kit = kit;
    }

}
