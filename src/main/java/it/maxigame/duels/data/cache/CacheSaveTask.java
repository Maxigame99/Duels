package it.maxigame.duels.data.cache;

import it.maxigame.duels.Duels;
import org.bukkit.scheduler.BukkitRunnable;

public class CacheSaveTask extends BukkitRunnable {

    private final CacheHandler cache;
    private final int timer;

    /**
     * Autosave caches of a CacheHandler object
     * @param cache The object
     * @param timer The time to wait between a save (in seconds)
     */
    public CacheSaveTask(CacheHandler cache, int timer) {
        this.cache = cache;
        this.timer = timer;
    }

    public void start() {
        long timer = this.timer * 20L;
        this.runTaskTimerAsynchronously(Duels.getInstance(), timer, timer);
    }

    @Override
    public void run() {
        cache.saveCaches();
    }
}
