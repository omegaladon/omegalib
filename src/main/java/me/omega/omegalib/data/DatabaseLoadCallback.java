package me.omega.omegalib.data;

public record DatabaseLoadCallback<V>(DatabaseLoadResult result, V value) {
}
