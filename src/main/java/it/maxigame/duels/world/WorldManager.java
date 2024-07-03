package it.maxigame.duels.world;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

@Getter
public class WorldManager {

    private static final String DUELS_WORLD = "duels";
    private World world;


    public WorldManager() {
        worldInit();
    }


    private void worldInit() {
        World world = Bukkit.getWorld(DUELS_WORLD);
        if (world == null) {
            world = Bukkit.createWorld(new WorldCreator(DUELS_WORLD)
                    .type(WorldType.FLAT)
                    .generateStructures(false)
                    .generatorSettings(""));
        }
        this.world = world;
    }

}
