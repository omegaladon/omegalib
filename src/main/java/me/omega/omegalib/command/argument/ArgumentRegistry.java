package me.omega.omegalib.command.argument;

import lombok.experimental.UtilityClass;
import me.omega.omegalib.command.argument.impl.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * A utility class that holds a map of argument parsers for various types. This class is used for parsing arguments
 * inside custom commands.
 */
@UtilityClass
public class ArgumentRegistry {

    /**
     * A map that holds the argument parsers for various types.
     */
    private static final HashMap<Class<?>, ArgumentParser<?>> PARSERS = new HashMap<>();

    static {
        PARSERS.put(Integer.class, new IntParser());
        PARSERS.put(Double.class, new DoubleParser());
        PARSERS.put(Boolean.class, new BooleanParser());
        PARSERS.put(String.class, new StringParser());
        PARSERS.put(Player.class, new PlayerParser());
        PARSERS.put(OfflinePlayer.class, new OfflinePlayerParser());
        PARSERS.put(Material.class, new MaterialParser());
    }

    /**
     * Returns the argument parser for the given class.
     *
     * @param clazz the class for which the argument parser is required
     * @return the argument parser for the given class
     */
    public static ArgumentParser<?> getParser(Class<?> clazz) {
        return PARSERS.get(clazz);
    }

}
