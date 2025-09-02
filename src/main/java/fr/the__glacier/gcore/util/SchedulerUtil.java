package fr.the__glacier.gcore.util;

import fr.the__glacier.gcore.GCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class SchedulerUtil {
    private static final boolean isFolia = GCore.folia();

    @Deprecated
    public static void runTaskAtFixedRate(Plugin plugin, Runnable task, long delay, long period){
        runTaskAtFixedRate(plugin, task, delay, period, null);
    }
    public static void runTaskAtFixedRate(Plugin plugin, Runnable task, long delay, long period, @Nullable Location location){
        if (isFolia){
            if (location == null){
                plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, foliaTask -> task.run(), delay, period);
            } else {
                plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, location, foliaTask -> task.run(), delay, period);
            }
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        }
    }

    @Deprecated
    public static void runTaskMultipleTime(Plugin plugin, Runnable runnable, long delay, long period, int count){
        runTaskMultipleTime(plugin, runnable, delay, period, count, null);
    }
    public static void runTaskMultipleTime(Plugin plugin, Runnable runnable, long delay, long period, int count, @Nullable Location location){
        final int[] counts = {count};
        if (isFolia){
            if (location == null){
                plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, foliaTask -> {
                    runnable.run();
                    counts[0]--;
                    if (counts[0] == 0) foliaTask.cancel();
                }, delay, period);
            } else {
                plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, location, foliaTask -> {
                    runnable.run();
                    counts[0]--;
                    if (counts[0] == 0) foliaTask.cancel();
                }, delay, period);
            }
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task -> {
                runnable.run();
                counts[0]--;
                if (counts[0] == 0) task.cancel();
            }, delay, period);
        }
    }

    @Deprecated
    public static void runTaskDelayed(Plugin plugin, Runnable task, long delay){
        runTaskDelayed(plugin, task, delay, null);
    }
    public static void runTaskDelayed(Plugin plugin, Runnable task, long delay, @Nullable Location location){
        if (isFolia){
            if (location == null){
                plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, foliaTask -> task.run(), delay);
            } else {
                plugin.getServer().getRegionScheduler().runDelayed(plugin, location, foliaTask -> task.run(), delay);
            }
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }

    @Deprecated
    public static void runTask(Plugin plugin, Runnable task){
        runTask(plugin, task, null);
    }
    public static void runTask(Plugin plugin, Runnable task, Location location){
        if (isFolia){
            if (location == null){
                plugin.getServer().getGlobalRegionScheduler().execute(plugin, task);
            } else {
                plugin.getServer().getRegionScheduler().execute(plugin, location, task);
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
}
