package me.omega.omegalib.command.argument;

/**
 * A record representing a command argument consisting of an {@link ArgumentParser} and a boolean flag indicating
 * whether the argument is optional.
 *
 * @param parser   the parser used to parse the argument
 * @param optional true if the argument is optional, false otherwise
 */
public record CommandArgument(ArgumentParser<?> parser, boolean optional) {
}
