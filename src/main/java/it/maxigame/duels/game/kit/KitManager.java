package it.maxigame.duels.game.kit;

import java.util.ArrayList;
import java.util.HashMap;

public class KitManager {

    private static HashMap<String, KitHolder> kits = new HashMap<>();


    public static ArrayList<KitHolder> getKits() {
        return new ArrayList<>(kits.values());
    }

    public static boolean register(KitHolder kit) {
        String name = kit.getName();
        if (kits.containsKey(name))
            return false;
        kits.put(name, kit);
        return true;
    }

    public static void unregister(String name) {
        kits.remove(name);
    }
}
