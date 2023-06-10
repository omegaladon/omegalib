package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;
import me.omega.omegalib.scheduling.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerParser extends ArgumentParser<OfflinePlayer> {

    @Override
    public OfflinePlayer parse(String input) {
        final OfflinePlayer[] offlinePlayer = new OfflinePlayer[1];
        Scheduler.async().run(() -> offlinePlayer[0] = Bukkit.getOfflinePlayer(input));
        return offlinePlayer[0];
    }

    @Override
    public Class<?> getType() {
        return OfflinePlayer.class;
    }

}
