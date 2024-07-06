package it.maxigame.duels.game.arena;

import it.maxigame.duels.Duels;
import it.maxigame.duels.serializers.ArenaSerializer;
import it.maxigame.duels.world.WorldCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ArenaAgent {
    private static final String ARENA_LOCATIONS_PATH = "arena-locations";
    private static final HashMap<String, Arena> arenas = new HashMap<>();


    public static void laodArenas() {
        System.out.println("§eLoading all arenas...");
        List<String> serializedArenas = Duels.getConfigYaml().getStringList(ARENA_LOCATIONS_PATH);
        if (!serializedArenas.isEmpty()) {
            System.out.println("§e+ Deserializing all arenas...");
            serializedArenas.stream().map(ArenaSerializer::deserialize).filter(Objects::nonNull).forEach(a->arenas.put(a.getName(), a));
        }
        System.out.println("§eDone!");
    }
    public static void saveArenas() {
        System.out.println("§eSaving all arenas...");
        System.out.println("§e+ Serializing all arenas...");
        List<String> serializedArenas = arenas.values().stream().map(ArenaSerializer::serialize).toList();
        System.out.println("§e+ Writing...");
        Duels.getConfigYaml().set(ARENA_LOCATIONS_PATH, serializedArenas);
        Duels.getInstance().saveConfig();
        System.out.println("§eDone!");
    }


    public static Arena getArena(String name) {
        return arenas.get(name);
    }
    public static Set<String> getArenasName() {
        return arenas.keySet();
    }
    public static boolean addArena(Arena arena) {
        return arenas.put(arena.getName(), arena)!=null;
    }
    public static void deleteArena(String arenaName) {
        arenas.remove(arenaName);
    }

    public static void clearArena(Arena arena) {
        arena.setStatus(ArenaStatus.CLEANING);
        Location loc1 = arena.getRegion1();
        Location loc2 = arena.getRegion2();
        World world = WorldCreator.getWorld();
        Bukkit.getScheduler().runTaskAsynchronously(Duels.getInstance(), ()->{
            world.getEntities().stream().filter(e->isInside(e.getLocation(), loc1, loc2)).forEach(e->Bukkit.getScheduler().runTask(Duels.getInstance(), e::remove));
            arena.setStatus(ArenaStatus.READY);
        });

    }
    private static boolean isInside(Location loc, Location l1, Location l2) {
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());

        return loc.getX() >= x1 && loc.getX() <= x2 && loc.getZ() >= z1 && loc.getZ() <= z2;
    }

    public synchronized static Arena calculareArena() {
        for (Arena arena : arenas.values()) {
            if (arena.getStatus()==ArenaStatus.READY)
                return arena;
        }
        return null;
    }
}
