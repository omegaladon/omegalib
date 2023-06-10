package me.omega.omegalib.command;

import org.bukkit.entity.Player;

/**
 * A record representing the context of a command execution.
 *
 * @param player      the player that executed the command
 * @param commandData the {@link CommandData} object representing the {@link MainCommand} that was executed
 * @param arguments   an array of objects representing the arguments used in the command
 * @param executed    the {@link CommandData} object representing the command, {@link SubCommand} or
 *                    {@link MainCommand}, that was executed
 */
public record CommandContext(Player player, CommandData commandData, Object[] arguments, CommandData executed) {
}
