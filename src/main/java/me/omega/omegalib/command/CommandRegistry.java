package me.omega.omegalib.command;

import lombok.AllArgsConstructor;
import me.omega.omegalib.command.argument.ArgumentParser;
import me.omega.omegalib.command.argument.ArgumentRegistry;
import me.omega.omegalib.command.argument.CommandArgument;
import me.omega.omegalib.command.completions.CompletionRegistry;
import me.omega.omegalib.command.completions.TabCompletion;
import me.omega.omegalib.plugin.AbstractJavaPlugin;
import me.omega.omegalib.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * A class responsible for registering custom commands with the Bukkit Server's commandMap. This class provides methods
 * to register AbstractCommand objects and their subcommands, and also to retrieve a map of registered commands and
 * their associated metadata.
 * <p/>
 * <p/>
 * To use, simply call the static method `register()` and pass in an instance of an AbstractCommand object to register
 * the command with the server.
 */
@AllArgsConstructor
public class CommandRegistry {

    private static final Map<Command, CommandData> commands = new HashMap<>();
    private static final CommandMap COMMAND_MAP;
    private static final Constructor<PluginCommand> PLUGIN_COMMAND_CONSTRUCTOR;

    static {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            COMMAND_MAP = (CommandMap) commandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            PLUGIN_COMMAND_CONSTRUCTOR = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            PLUGIN_COMMAND_CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Command, CommandData> getCommands() {
        return commands;
    }

    private final AbstractJavaPlugin plugin;

    /**
     * Registers a command by processing a main command and any subcommand methods annotated with {@link SubCommand},
     * creating a {@link CommandData} object for each. Then, registers the main command with the server's CommandMap,
     * making the command executable.
     *
     * @param command the command to be registered
     * @throws IllegalStateException if there are 0 or multiple main commands or if any command is invalid or a
     *                               duplicate
     * @throws RuntimeException      if there is an error registering the command with the server's CommandMap
     */
    public void register(Command command) {

        Class<? extends Command> commandClass = command.getClass();

        List<Method> methods = Arrays.asList(commandClass.getDeclaredMethods());
        List<Method> mainCommands =
                methods.stream().filter(method -> method.isAnnotationPresent(MainCommand.class)).toList();

        if (mainCommands.size() > 1) {
            throw new IllegalStateException("Found 2 or more methods annotated with @MainCommand.");
        }

        if (mainCommands.size() == 0) {
            throw new IllegalStateException("Found no method annotated with @MainCommand.");
        }

        for (Method method : methods) {
            if (!method.isAnnotationPresent(SubCommand.class) || !method.isAnnotationPresent(MainCommand.class))
                continue;
            validateMethod(method, commandClass);
        }

        Method mainMethod = mainCommands.get(0);
        MainCommand mainCommandAnnotation = mainMethod.getAnnotation(MainCommand.class);

        CommandData mainCommand = new CommandData(
                mainCommandAnnotation.name(),
                List.of(mainCommandAnnotation.aliases()),
                mainCommandAnnotation.permission(),
                processParsers(mainCommandAnnotation.arguments(), mainCommandAnnotation.optional(), commandClass),
                processCompletions(mainCommandAnnotation.completions(), command),
                false,
                null,
                mainCommandAnnotation.onlyPlayers(),
                Reflection.getMethod(commandClass, mainMethod.getName(), CommandContext.class)
        );

        mainCommand.setSubcommands(processSubcommands(commandClass, mainCommand, command));
        command.setCommandData(mainCommand);
        commands.put(command, mainCommand);

        List<String> mainCommandAliases = new ArrayList<>();
        mainCommandAliases.add(mainCommand.getName());
        mainCommandAliases.addAll(mainCommand.getAliases());

        for (String alias : mainCommandAliases) {
            try {
                PluginCommand pluginCommand = PLUGIN_COMMAND_CONSTRUCTOR.newInstance(alias, plugin);

                COMMAND_MAP.register(plugin.getName(), pluginCommand);

                pluginCommand.setLabel(alias.toLowerCase());
                pluginCommand.setExecutor(command);
                pluginCommand.setTabCompleter(command);

                if (!Objects.equals(mainCommand.getPermission(), "")) {
                    pluginCommand.setPermission(mainCommand.getPermission());
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Recursively processes the methods of a given class that are annotated with the {@link SubCommand} annotation, and
     * creates a list of {@link CommandData} objects representing each subcommand.
     *
     * @param commandClass the class containing the subcommands to be processed
     * @param parent       the parent command to which the commands will belong
     * @return a List of CommandData objects representing each subcommand
     * @throws IllegalArgumentException if a subcommand is invalid or a duplicate
     */
    private List<CommandData> processSubcommands(Class<?> commandClass, CommandData parent, Command command) {
        List<CommandData> subcommands = new ArrayList<>();
        for (Method method : commandClass.getMethods()) {
            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            validateMethod(method, commandClass);
            SubCommand subcommand = method.getAnnotation(SubCommand.class);

            if (!subcommand.parent().equals(parent.getName())) continue;

            CommandData subcommandCommand = new CommandData(
                    subcommand.name(),
                    List.of(subcommand.aliases()),
                    subcommand.permission(),
                    processParsers(subcommand.arguments(), subcommand.optional(), commandClass),
                    processCompletions(subcommand.completions(), command),
                    true,
                    parent,
                    subcommand.onlyPlayers(),
                    Reflection.getMethod(commandClass, method.getName(), CommandContext.class)
            );
            subcommandCommand.setSubcommands(processSubcommands(commandClass, subcommandCommand, command));
            subcommands.add(subcommandCommand);
        }
        return subcommands;
    }

    /**
     * Processes the provided list of {@link ArgumentParser} classes, optional flags, and a target class, creating a
     * list of ArgumentParser instances paired with their respective optional flags.
     *
     * @param parsers      an array of ArgumentParser classes to process
     * @param optional     an array of boolean flags indicating whether each ArgumentParser is optional or not
     * @param commandClass the target class where the ArgumentParsers are being processed
     * @return a List of Pairs, where each Pair consists of an ArgumentParser instance and a boolean flag indicating
     * whether it is optional
     * @throws IllegalStateException if an invalid ArgumentParser is found or an optional ArgumentParser is not the last
     *                               in the list
     */
    private List<CommandArgument> processParsers(Class<?>[] parsers, boolean[] optional, Class<?> commandClass) {
        List<CommandArgument> argumentParsers = new ArrayList<>();

        if (parsers.length == 0) return argumentParsers;

        if (optional.length != parsers.length)
            throw new IllegalStateException("Optional array should be the same length as the parsers array in class " + commandClass.getSimpleName() + ".");

        for (int i = 0; i < parsers.length; i++) {
            Class<?> argumentClass = parsers[i];
            ArgumentParser<?> parser = ArgumentRegistry.getParser(argumentClass);
            if (parser == null) {
                throw new IllegalStateException("Found invalid argument parser " + argumentClass.getSimpleName() + " " +
                                                "in class " + commandClass.getSimpleName() + ".");
            }
            if (optional[i] && i != parsers.length - 1) {
                throw new IllegalStateException("Found optional argument parser " + argumentClass.getSimpleName() +
                                                " in class " + commandClass.getSimpleName() + " that is not " +
                                                "the last argument.");
            }
            argumentParsers.add(new CommandArgument(parser, optional[i]));
        }
        return argumentParsers;
    }

    private List<TabCompletion> processCompletions(String completionString, Command command) {

        if (completionString.equals("")) return new ArrayList<>();

        List<TabCompletion> completions = new ArrayList<>();
        for (String completion : completionString.split(" ")) {
            completions.add(CompletionRegistry.getCompletion(completion, command));
        }
        return completions;
    }

    /**
     * Validates that the given method has the correct parameters for a command method.
     *
     * @param method       the method to validate
     * @param commandClass the class containing the method
     * @throws IllegalStateException if the method does not have the correct parameters ({@link CommandContext})
     */
    private void validateMethod(Method method, Class<?> commandClass) {
        if (method.getParameterCount() != 1
            || !method.getParameterTypes()[0].equals(CommandContext.class)) {
            throw new IllegalStateException("Method " + method.getName() + " in class " + commandClass.getSimpleName() + " does not have the correct signature. Expected one parameter, CommandContext.");
        }
    }

}
