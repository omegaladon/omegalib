package me.omega.omegalib.menu;

import lombok.NonNull;
import org.bukkit.event.Event;

@FunctionalInterface
public interface MenuAction<T extends Event> {

    void execute(@NonNull T event);

}
