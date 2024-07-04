package it.maxigame.duels.serializers;

import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.serializers.error.InvalidSerializationException;
import org.bukkit.Location;

public class ArenaSerializer {

    public static String serialize(Arena arena) {
        String name = arena.getName();
        String region1 = LocationSerializer.serialize(arena.getRegion1());
        String region2 = LocationSerializer.serialize(arena.getRegion2());
        String spawn1 = LocationSerializer.serialize(arena.getSpawn1());
        String spawn2 = LocationSerializer.serialize(arena.getSpawn2());

        return String.format("%s;%s;%s;%s", name, region1, region2, spawn1, spawn2);
    }

    public static Arena deserialize(String arena) {
        String[] args = arena.split(";");
        try {
            if (args.length < 5)
                throw new InvalidSerializationException("Invalid serialization for: "+Arena.class.getName());
            String name = args[0];
            Location region1 = LocationSerializer.deserialize(args[1]);
            Location region2 = LocationSerializer.deserialize(args[2]);
            Location spawn1 = LocationSerializer.deserialize(args[3]);
            Location spawn2 = LocationSerializer.deserialize(args[4]);
            if (region1==null || region2==null || spawn1==null || spawn2==null)
                throw new InvalidSerializationException("Invalid serialization for: "+Location.class.getName());
            return new Arena(name, region1, region2, spawn1, spawn2);
        }catch (InvalidSerializationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
