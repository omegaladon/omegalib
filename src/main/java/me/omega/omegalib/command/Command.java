package me.omega.omegalib.command;

import lombok.Getter;
import lombok.Setter;
import me.omega.omegalib.command.argument.CommandArgument;
import me.omega.omegalib.utils.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A class that implements the TabExecutor interface for Bukkit commands. This class is used for creating custom
 * commands.
 */
@Getter
@Setter
public abstract class Command implements TabExecutor {

    private CommandData commandData;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command,
                             @NotNull String name, @NotNull String[] args) {

        CommandData executedData;

        if (!(commandSender instanceof Player)) {
            if (commandData.isOnlyPlayers()) {
                Messenger.message(commandSender, Messenger.MessageType.ERROR, "Only players can use this command.");
                return true;
            }
        }

        Player player = (Player) commandSender;
        Command instance =
                CommandRegistry.getCommands().keySet().stream().filter(cmd -> cmd.getCommandData().equals(commandData)).findFirst().orElse(null);

        if (instance == null) {
            throw new IllegalStateException("Could not find instance of command " + commandData.getName() + ".");
        }

        executedData = searchForSubcommand(commandData, args);
        if (executedData == null) {
            Messenger.message(player, Messenger.MessageType.ERROR, "Subcommand " + args[0] + " not found for main " +
                                                                   "command " + commandData.getName() + ".");
            return true;
        }

        // Handle permissions
        if (!Objects.equals(executedData.getPermission(), "") && !player.hasPermission(executedData.getPermission())) {
            Messenger.message(player, Messenger.MessageType.ERROR, "You do not have permission to use this command.");
            return true;
        }
        if (!Objects.equals(executedData.getPermission(), "") && !Objects.equals(commandData.getPermission(), "") && !player.hasPermission(commandData.getPermission())) {
            Messenger.message(player, Messenger.MessageType.ERROR, "You do not have permission to use this command.");
            return true;
        }

        // Handle arguments
        Object[] newArgs = new Object[executedData.getArguments().size()];
        for (int i = 0; i < executedData.getArguments().size(); i++) {
            CommandArgument argument = executedData.getArguments().get(i);
            int argIndex = i + executedData.getName().split(" ").length;

            if (i == executedData.getArguments().size() - 1 && argument.parser().getType() == String.class && argument.optional()) {
                String[] remainingArgs = Arrays.copyOfRange(args, argIndex, args.length);
                if (remainingArgs.length == 0) {
                    newArgs[i] = null;
                    continue;
                }
                newArgs[i] = String.join(" ", remainingArgs);
                break;
            }

            if (argument.optional() && args.length <= argIndex) {
                newArgs[i] = null;
                continue;
            }

            argIndex -= 1;

            try {
                String argToParse = args[argIndex];
                newArgs[i] = argument.parser().parse(argToParse);
            } catch (Exception e) {
                String usage = getUsage(executedData);
                Messenger.message(player, Messenger.MessageType.ERROR, "Invalid argument at position " + (i + 1) + "." +
                                                                       " Expected usage: " + usage);
                return true;
            }

            if (newArgs[i] == null) {
                String usage = getUsage(executedData);
                Messenger.message(player, Messenger.MessageType.ERROR, "Invalid argument at position " + (i + 1) + "." +
                                                                       " Expected usage: " + usage);
                return true;
            }
        }

        CommandContext commandContext = new CommandContext(
                player,
                commandData,
                newArgs,
                executedData
        );

        try {
            executedData.getMethod().invoke(instance, commandContext);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
                                                @NotNull org.bukkit.command.Command command, @NotNull String name,
                                                @NotNull String[] args) {

        CommandData executedData;

        Player player = (Player) commandSender;
        Command instance =
                CommandRegistry.getCommands().keySet().stream().filter(cmd -> cmd.getCommandData().equals(commandData)).findFirst().orElse(null);

        if (instance == null) {
            throw new IllegalStateException("Could not find instance of command " + commandData.getName() + ".");
        }

        executedData = searchForSubcommand(commandData, args);
        if (executedData == null) {
            return null;
        }

        if (!Objects.equals(executedData.getPermission(), "") && !player.hasPermission(executedData.getPermission())) {
            return null;
        }
        if (!Objects.equals(executedData.getPermission(), "") && !Objects.equals(commandData.getPermission(), "") && !player.hasPermission(commandData.getPermission())) {
            return null;
        }

        List<String> completions = new ArrayList<>();
        for (CommandData subcommand : executedData.getSubcommands()) {
            if (Objects.equals(subcommand.getPermission(), "") || player.hasPermission(subcommand.getPermission())) {
                completions.add(subcommand.getName());
                completions.addAll(subcommand.getAliases());
            }
        }

        int currentArgIndex = args.length - 1;

        if (currentArgIndex < 0) {
            return completions;
        }
        if (currentArgIndex >= executedData.getArguments().size()) {
            return null;
        }

        String lastArg = args[currentArgIndex];

        List<String> availableCompletions = executedData.getCompletions().get(currentArgIndex).getFiltered(lastArg);
        if (availableCompletions == null) {
            return null;
        }
        completions.addAll(availableCompletions);

        return completions;

    }

    /**
     * Constructs a usage string for the command specified by the given {@link CommandData} object.
     *
     * @param executedData the {@link CommandData} object representing the executed command
     * @return a {@code String} containing the usage information for the executed command
     */
    private String getUsage(CommandData executedData) {
        StringBuilder usage = new StringBuilder("/" + commandData.getName());

        CommandData currentData = executedData;
        List<String> subcommandNames = new ArrayList<>();
        while (currentData.isSubcommand()) {
            subcommandNames.add(0, currentData.getName());
            currentData = currentData.getParent();
        }

        for (String subcommandName : subcommandNames) {
            usage.append(" ").append(subcommandName);
        }

        List<CommandArgument> arguments = executedData.getArguments();
        for (CommandArgument cmdArgument : arguments) {
            if (cmdArgument.optional()) {
                usage.append(" [").append(cmdArgument.parser().getType().getSimpleName()).append("]");
            } else {
                usage.append(" <").append(cmdArgument.parser().getType().getSimpleName()).append(">");
            }
        }
        return usage.toString();
    }

    /**
     * Searches for a subcommand that matches the specified arguments in the given {@link CommandData} object.
     *
     * @param data the {@link CommandData} object to search for the subcommand in
     * @param args the arguments containing the subcommand and any subsequent subcommands
     * @return the {@link CommandData} object representing the subcommand if it is found, or the original
     * {@link CommandData} object if it is not found
     */
    private CommandData searchForSubcommand(CommandData data, String[] args) {
        if (args.length == 0) {
            return data;
        }
        CommandData matchedSubcommand = null;
        for (CommandData subcommand : data.getSubcommands()) {
            if (!subcommand.getAliases().contains(args[0]) && !subcommand.getName().equals(args[0])) {
                continue;
            }
            matchedSubcommand = subcommand;
            break;
        }
        if (matchedSubcommand == null) {
            return data;
        }
        if (args.length == 1) {
            return matchedSubcommand;
        }
        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);
        return searchForSubcommand(matchedSubcommand, remainingArgs);
    }

}
