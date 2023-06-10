package me.omega.omegalib.command.completions;

import lombok.Getter;
import me.omega.omegalib.command.Command;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An abstract class representing a tab completion for a command parameter or argument.
 */
@Getter
public abstract class TabCompletion {

    /**
     * The type of tab completion.
     */
    private final CompletionType type;

    /**
     * The value of the tab completion.
     */
    private final String value;

    /**
     * The instance of the command this tab completion is associated with.
     */
    private final Command instance;

    /**
     * Constructs a new TabCompletion with the specified type, value, and command instance.
     *
     * @param type     the type of this tab completion
     * @param value    the value of this tab completion
     * @param instance the command instance associated with this tab completion
     */
    public TabCompletion(String type, String value, Command instance) {
        this.type = CompletionType.valueOf(type);
        this.value = value;
        this.instance = instance;
    }

    /**
     * Returns a list of possible completions for this tab completion.
     *
     * @return a list of possible completions
     */
    public abstract List<String> get();

    /**
     * Returns a filtered list of possible completions for this tab completion, based on the specified input.
     *
     * @param input the input to filter the list by
     * @return a filtered list of possible completions
     */
    public List<String> getFiltered(String input) {
        return get().stream().filter(s -> s.toLowerCase().contains(input.toLowerCase())).collect(Collectors.toList());
    }

}
