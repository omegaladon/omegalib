package me.omega.omegalib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to mark a method as a main command for a plugin.
 *
 * <p>Methods annotated with this annotation should have the following signature:</p>
 *
 * <pre>{@code
 * public void commandName(CommandContext context);
 * }</pre>
 *
 * <p>where {@code commandName} is the name of the command, and {@code CommandContext} is a class representing the
 * command's context, containing information such as the sender, executed command, arguments, and more.</p>
 *
 * @see CommandContext
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MainCommand {

    /**
     * The name of the command.
     *
     * @return the name of the command
     */
    String name();

    /**
     * An array of aliases for the command.
     *
     * @return an array of aliases for the command
     */
    String[] aliases() default {};

    /**
     * The permission required to use the command.
     *
     * @return the permission required to use the command
     */
    String permission() default "";

    /**
     * An array of classes representing the types of arguments expected by the command.
     *
     * @return an array of classes representing the types of arguments expected by the command
     */
    Class<?>[] arguments() default {};

    /**
     * A string representing the completions for the command.
     *
     * @return a string representing the completions for the command
     */
    String completions() default "";

    /**
     * An array of booleans indicating whether each argument is optional or not.
     *
     * @return an array of booleans indicating whether each argument is optional or not
     */
    boolean[] optional() default {};

    /**
     * Whether the command can only be used by players (not console).
     *
     * @return true if the command can only be used by players, false otherwise
     */
    boolean onlyPlayers() default false;
}

