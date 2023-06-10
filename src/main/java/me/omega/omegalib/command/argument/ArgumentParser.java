package me.omega.omegalib.command.argument;

/**
 * An abstract class for parsing command arguments of a given type T.
 *
 * @param <T> the type of argument to parse
 */
public abstract class ArgumentParser<T> {

    /**
     * Parses the given input string and returns the corresponding argument of type {@code T}.
     *
     * @param input the input string to parse
     * @return the parsed argument of type {@code T}
     * @throws Exception if the input string cannot be parsed
     */
    public abstract T parse(String input) throws Exception;


    /**
     * Returns the type of argument this parser tries parses.
     *
     * @return the type of argument this parser tries parses
     */
    public abstract Class<?> getType();

}
