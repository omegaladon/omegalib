package me.omega.omegalib.command.completions.impl;

import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayersCompletion extends TabCompletion {

    public PlayersCompletion(String type, String value, Command instance) {
        super(type, value, instance);
    }

    @Override
    public List<String> get() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
