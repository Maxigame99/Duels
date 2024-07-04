package it.maxigame.duels.game.arena;

import it.maxigame.duels.Duels;
import it.maxigame.duels.serializers.ArenaSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaAgent {
    private static final String ARENA_LOCATIONS_PATH = "arena-locations";
    private static final ArrayList<Arena> arenas = new ArrayList<>();


    public static void laodArenas() {
        System.out.println("§6Loading all arenas...");
        List<String> serializedArenas = Duels.getConfig().getStringList(ARENA_LOCATIONS_PATH);
        System.out.println("§e+ Deserializing all arenas...");
        arenas.addAll(serializedArenas.stream().map(ArenaSerializer::deserialize).filter(Objects::nonNull).toList());
        System.out.println("§eDone!");
    }
    public static void saveArenas() {
        System.out.println("§6Saving all arenas...");
        System.out.println("§e+ Serializing all arenas...");
        List<String> serializedArenas = arenas.stream().map(ArenaSerializer::serialize).toList();
        System.out.println("§e+ Writing...");
        Duels.getConfig().set(ARENA_LOCATIONS_PATH, serializedArenas);
        System.out.println("§eDone!");
    }


    public synchronized static Arena calculareArena() {
        for (Arena arena : arenas) {
            if (arena.getStatus()==ArenaStatus.READY)
                return arena;
        }
        return null;
    }
}
