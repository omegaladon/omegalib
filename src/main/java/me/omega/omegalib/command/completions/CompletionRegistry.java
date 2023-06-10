package me.omega.omegalib.command.completions;

import lombok.experimental.UtilityClass;
import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.impl.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * A utility class that stores a map of {@link CompletionType} to their corresponding {@link TabCompletion}
 * implementations, and provides a method to get a {@link TabCompletion} instance based on user input, containing class
 * and command.
 */
@UtilityClass
public class CompletionRegistry {

    /**
     * A map of {@link CompletionType} to their corresponding {@link TabCompletion} implementations.
     */
    private static final HashMap<CompletionType, Class<?>> COMPLETIONS = new HashMap<>();

    static {
        COMPLETIONS.put(CompletionType.STRINGS, StringsCompletion.class);
        COMPLETIONS.put(CompletionType.BOOLEAN, BooleanCompletion.class);
        COMPLETIONS.put(CompletionType.MATERIALS, MaterialsCompletion.class);
        COMPLETIONS.put(CompletionType.PLAYERS, PlayersCompletion.class);
        COMPLETIONS.put(CompletionType.INTS, IntsCompletion.class);
        COMPLETIONS.put(CompletionType.CUSTOM, CustomCompletion.class);
    }

    /**
     * Returns a {@link TabCompletion} instance based on user input and command.
     *
     * @param input   the user input string
     * @param command the command being completed
     * @return a {@link TabCompletion} instance corresponding to the given input
     * @throws IllegalArgumentException if the completion type specified in the input is invalid.
     * @throws RuntimeException         if the constructor of the corresponding {@link TabCompletion} implementation
     *                                  cannot be found or accessed, or if an error occurs while creating the instance
     */
    public static TabCompletion getCompletion(String input, Command command) {
        String type;
        String value;

        if (input.contains(":")) {
            type = input.split(":")[0].replace("@", "").toUpperCase();
            value = input.split(":")[1];
        } else {
            type = input.replace("@", "").toUpperCase();
            value = null;
        }

        CompletionType enumType;
        try {
            enumType = CompletionType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The completion type " + type + " is not valid.");
        }

        try {
            return (TabCompletion) COMPLETIONS.get(enumType).getConstructor(String.class, String.class,
                                                                            Command.class).newInstance(type, value,
                                                                                                       command);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
