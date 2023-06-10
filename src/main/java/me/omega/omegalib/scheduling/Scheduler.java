package me.omega.omegalib.scheduling;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;

/**
 * Scheduling utilities.
 */
@UtilityClass
public class Scheduler {

    private static JavaPlugin plugin;

    private static final SyncScheduler sync = new SyncScheduler();
    private static final AsyncScheduler async = new AsyncScheduler();

    public static SyncScheduler sync() {
        return sync;
    }

    public static AsyncScheduler async() {
        return async;
    }

    public static void init(JavaPlugin plugin) {
        Scheduler.plugin = plugin;
    }

    public static void cancelAll() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    public class SyncScheduler implements SchedulerType {

        @Override
        public CompletableFuture<Void> run(Runnable runnable) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            Bukkit.getScheduler().runTask(plugin, () -> {
                runnable.run();
                future.complete(null);
            });
            return future;
        }

        @Override
        public CompletableFuture<Void> runLater(Runnable runnable, long delay) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                runnable.run();
                future.complete(null);
            }, delay);
            return future;
        }


        @Override
        public BukkitTask runRepeating(Runnable runnable, long delay, long period) {
            return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        }

    }

    public class AsyncScheduler implements SchedulerType {

        @Override
        public CompletableFuture<Void> run(Runnable runnable) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                runnable.run();
                future.complete(null);
            });
            return future;
        }

        @Override
        public CompletableFuture<Void> runLater(Runnable runnable, long delay) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                runnable.run();
                future.complete(null);
            }, delay);
            return future;
        }


        @Override
        public BukkitTask runRepeating(Runnable runnable, long delay, long period) {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        }

    }

}
