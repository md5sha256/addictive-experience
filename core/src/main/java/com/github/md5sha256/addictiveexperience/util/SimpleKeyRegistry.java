package com.github.md5sha256.addictiveexperience.util;

import com.github.md5sha256.addictiveexperience.api.util.KeyRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SimpleKeyRegistry implements KeyRegistry {

    private final Map<String, Set<String>> keyMap = new HashMap<>();
    private final Map<String, Set<String>> valueMap = new HashMap<>();

    @Override
    public @NotNull Set<String> values(@NotNull final String namespace) {
        return Collections
                .unmodifiableSet(this.keyMap.getOrDefault(namespace, Collections.emptySet()));
    }

    @Override
    public @NotNull Set<String> keys() {
        return Collections.unmodifiableSet(this.keyMap.keySet());
    }

    @Override
    public @NotNull Set<String> keysForValue(@NotNull final String value) {
        return Collections.unmodifiableSet(this.valueMap.getOrDefault(value, Collections.emptySet()));
    }

    @Override
    public void register(@NotNull final Key key) {
        final Set<String> values = this.keyMap.computeIfAbsent(key.namespace(), x -> new HashSet<>());
        values.add(key.value());
        final Set<String> keys = this.valueMap.computeIfAbsent(key.value(), x -> new HashSet<>());
        keys.add(key.namespace());
    }


    public void remove(@NotNull final Key key) {
        final Set<String> setValues = this.keyMap.get(key.namespace());
        final Set<String> setKeys = this.valueMap.get(key.value());
        if (setValues != null) {
            setValues.remove(key.value());
            if (setValues.isEmpty()) {
                this.keyMap.remove(key.namespace());
            }
            setKeys.remove(key.namespace());
            if (setKeys.isEmpty()) {
                this.valueMap.remove(key.value());
            }
        }
    }
}
