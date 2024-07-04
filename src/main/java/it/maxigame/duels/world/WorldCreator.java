package it.maxigame.duels.world;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;

public class WorldCreator {

    private static final String DUELS_WORLD = "duels";
    @Getter
    private static World world;

    public static void worldInit() {
        System.out.println("§eInitializing world...");
        World world = Bukkit.getWorld(DUELS_WORLD);
        if (world == null) {
            world = Bukkit.createWorld(new org.bukkit.WorldCreator(DUELS_WORLD)
                    .type(WorldType.FLAT)
                    .generateStructures(false)
                    .generatorSettings(""));
        }
        WorldCreator.world = world;
        System.out.println("§eDone!");
    }

}
