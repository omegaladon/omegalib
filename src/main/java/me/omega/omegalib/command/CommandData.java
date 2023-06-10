package me.omega.omegalib.command;

import lombok.Data;
import me.omega.omegalib.command.argument.CommandArgument;
import me.omega.omegalib.command.completions.TabCompletion;
import me.omega.omegalib.utils.Reflection;

import java.util.List;

/**
 * A class representing the data of a custom command.
 */
@Data
public class CommandData {

    /**
     * The name of the command.
     */
    private final String name;

    /**
     * The list of aliases for the command.
     */
    private final List<String> aliases;

    /**
     * The permission required to execute the command.
     */
    private final String permission;

    /**
     * The list of arguments for the command.
     */
    private final List<CommandArgument> arguments;

    /**
     * The list of tab completions for the command.
     */
    private final List<TabCompletion> completions;

    /**
     * The list of subcommands for the command.
     */
    private List<CommandData> subcommands;

    /**
     * Whether this command is a subcommand of another command.
     */
    private final boolean isSubcommand;

    /**
     * The parent command of this command, if this is a subcommand.
     */
    private final CommandData parent;

    /**
     * Whether this command can only be executed by players.
     */
    private final boolean onlyPlayers;

    /**
     * The method this command should call when executed.
     */
    private final Reflection.MethodAccessor method;

}
