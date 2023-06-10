package me.omega.omegalib.scheduling;

import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;

public interface SchedulerType {

    CompletableFuture<Void> run(Runnable runnable);

    CompletableFuture<Void> runLater(Runnable runnable, long delay);

    BukkitTask runRepeating(Runnable runnable, long delay, long period);

}
