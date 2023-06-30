package me.omega.omegalib.scheduling;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class Scheduler {

    private final JavaPlugin plugin;

    private final SyncScheduler sync = new SyncScheduler();
    private final AsyncScheduler async = new AsyncScheduler();

    public SyncScheduler sync() {
        return sync;
    }

    public AsyncScheduler async() {
        return async;
    }

    public void cancelAll() {
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
