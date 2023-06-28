package me.omega.omegalib.utils;

import lombok.NonNull;

public record Pair<K, V>(@NonNull K key, @NonNull V value) {
}
