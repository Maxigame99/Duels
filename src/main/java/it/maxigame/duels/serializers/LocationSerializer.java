package it.maxigame.duels.serializers;

import it.maxigame.duels.serializers.error.InvalidSerializationException;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {
    public static String serialize(Location object) {
        if (object.getYaw() > 0 || object.getPitch() > 0 || object.getYaw() < 0 || object.getPitch() < 0) {
            return String.format("%s,%s,%s,%s,%s,%s", object.getWorld().getName(), object.getX(), object.getY(), object.getZ(), object.getYaw(), object.getPitch());
        }
        return String.format("%s,%s,%s,%s", object.getWorld().getName(), object.getX(), object.getY(), object.getZ());
    }

    public static Location deserialize(String data) {
        try {
            String[] split = data.split("[,]");
            if (split.length == 6) {
                return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
            } else if (split.length == 4) {
                return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
            } else {
                throw new InvalidSerializationException("Invalid serialization for " + LocationSerializer.class.getSimpleName());
            }
        } catch (InvalidSerializationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
