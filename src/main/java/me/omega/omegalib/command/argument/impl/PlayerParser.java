package me.omega.omegalib.command.argument.impl;

import me.omega.omegalib.command.argument.ArgumentParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerParser extends ArgumentParser<Player> {

    @Override
    public Player parse(String input) {
        return Bukkit.getServer().getPlayer(input);
    }

    @Override
    public Class<?> getType() {
        return Player.class;
    }

}
