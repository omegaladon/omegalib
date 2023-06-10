package me.omega.omegalib.menu;

import lombok.NonNull;
import org.bukkit.event.Event;

/**
 * A functional interface representing an action that can be executed in response to a menu event.
 *
 * @param <T> the type of event that this action takes as a parameter
 */
@FunctionalInterface
public interface MenuAction<T extends Event> {

    /**
     * Executes the action in response to a menu event.
     *
     * @param event the event that triggered the action
     */
    void execute(@NonNull T event);

}
