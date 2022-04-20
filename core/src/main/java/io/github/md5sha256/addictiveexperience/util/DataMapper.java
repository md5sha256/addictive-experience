package io.github.md5sha256.addictiveexperience.util;

import io.github.md5sha256.addictiveexperience.api.util.DataKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataMapper<K> {

    private final Map<K, Map<DataKey<?>, ?>> parentMap = new HashMap<>();

    public DataMapper() {

    }

    public DataMapper(@NotNull DataMapper<K> other) {
        this.parentMap.putAll(other.mapCopy());
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull Optional<@NotNull T> get(@NotNull K key, @NotNull DataKey<T> dataKey) {
        final Map<DataKey<?>, ?> map = this.parentMap.get(key);
        if (map == null) {
            return Optional.empty();
        }
        final Object o = map.get(dataKey);
        if (o == null) {
            return Optional.empty();
        }
        return Optional.of((T) o);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> void set(@NotNull K key, @NotNull DataKey<T> dataKey, @NotNull T value) {
        final Map map = this.parentMap.computeIfAbsent(key, x -> new HashMap<>());
        map.put(dataKey, value);
    }

    public boolean containsKey(@NotNull K key) {
        return this.parentMap.containsKey(key);
    }

    public <T> boolean containsKey(@NotNull K key, @NotNull DataKey<T> dataKey) {
        final Map<DataKey<?>, ?> map = this.parentMap.get(key);
        return map != null && map.containsKey(dataKey);
    }

    public void clear() {
        this.parentMap.clear();
    }

    public void clear(@NotNull K key) {
        this.parentMap.remove(key);
    }

    public <T> void clear(@NotNull K key, @NotNull DataKey<T> dataKey) {
        final Map<DataKey<?>, ?> map = this.parentMap.get(key);
        if (map != null) {
            map.remove(dataKey);
            if (map.isEmpty()) {
                this.parentMap.remove(key);
            }
        }

    }

    public <T> void clear(@NotNull DataKey<T> dataKey) {
        for (Map<DataKey<?>, ?> value : this.parentMap.values()) {
            value.remove(dataKey);
        }
        this.parentMap.values().removeIf(Map::isEmpty);
    }

    public @NotNull Map<@NotNull K, @NotNull Map<@NotNull DataKey<?>, ?>> mapCopy() {
        final Map<K, Map<DataKey<?>, ?>> copy = new HashMap<>(this.parentMap.size());
        for (Map.Entry<K, Map<DataKey<?>, ?>> entry : this.parentMap.entrySet()) {
            final Map<DataKey<?>, ?> mapCopy = new HashMap<>(entry.getValue());
            copy.put(entry.getKey(), mapCopy);
        }
        return copy;
    }

}
