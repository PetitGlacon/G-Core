package fr.the__glacier.gcore.util;

import fr.the__glacier.gcore.GCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SchedulerUtil {
    private static final boolean isFolia = GCore.getInstance().folia();

    public static void runTaskAtFixedRate(Plugin plugin, Runnable task, long delay, long period){
        if (isFolia){
            plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, foliaTask -> task.run(), delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        }
    }

    public static void runTaskMultipleTime(Plugin plugin, Runnable runnable, long delay, long period, int count){
        final int[] counts = {count};
        if (isFolia){
            plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, foliaTask -> {
                runnable.run();
                counts[0]--;
                if (counts[0] == 0) foliaTask.cancel();
            }, delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task -> {
                runnable.run();
                counts[0]--;
                if (counts[0] == 0) task.cancel();
            }, delay, period);
        }
    }

    public static void runTaskDelayed(Plugin plugin, Runnable task, long delay){
        if (isFolia){
            plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, foliaTask -> task.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }
    public static void runTask(Plugin plugin, Runnable task){
        if (isFolia){
            plugin.getServer().getGlobalRegionScheduler().run(plugin, foliaTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
}
