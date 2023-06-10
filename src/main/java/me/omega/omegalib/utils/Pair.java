package me.omega.omegalib.utils;

import lombok.NonNull;

/**
 * Represents a pair of values of two types.
 *
 * @param <K> the type of the first value
 * @param <V> the type of the second value
 */
public record Pair<K, V>(@NonNull K key, @NonNull V value) {
}
