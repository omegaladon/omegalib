package me.omega.omegalib.plugin.register;

/**
 * An enumeration of Minecraft API versions.
 */
public enum ApiVersion {
    V1_19,
    V1_18,
    V1_17,
    V1_16,
    V1_15,
    V1_14,
    V1_13;

    public String toString() {
        return name().substring(1).replace('_', '.');
    }
}
