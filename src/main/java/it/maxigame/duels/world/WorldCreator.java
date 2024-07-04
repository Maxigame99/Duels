package it.maxigame.duels.world;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;

@Getter
public class WorldCreator {

    private static final String DUELS_WORLD = "duels";
    private World world;


    public WorldCreator() {
        worldInit();
    }


    private void worldInit() {
        World world = Bukkit.getWorld(DUELS_WORLD);
        if (world == null) {
            world = Bukkit.createWorld(new org.bukkit.WorldCreator(DUELS_WORLD)
                    .type(WorldType.FLAT)
                    .generateStructures(false)
                    .generatorSettings(""));
        }
        this.world = world;
    }

}
