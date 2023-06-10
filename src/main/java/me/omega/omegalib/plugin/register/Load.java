package me.omega.omegalib.plugin.register;

import java.util.Locale;

/**
 * An enumeration of the stages in which a plugin can be loaded.
 */
public enum Load {
    STARTUP,
    POSTWORLD;

    public String toString() {
        return this.name().toUpperCase(Locale.ROOT);
    }
}
