package it.maxigame.duels.game.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
public class Arena {
    private final String name;
    private ArenaStatus status = ArenaStatus.READY;

    @NotNull private Location region1;
    @NotNull private Location region2;

    @NotNull private Location spawn1;
    @NotNull private Location spawn2;

    public Arena(String name, @NotNull Location region1, @NotNull Location region2, @NotNull Location spawn1, @NotNull Location spawn2) {
        this.name = name;
        this.region1 = region1;
        this.region2 = region2;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
    }

}
