package com.github.md5sha256.addictiveexperience.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class EnumPolyStateful<T, S extends Enum<S>> implements Stateful<T, S> {

    private final Map<S, T> stateMap;
    private final Set<S> states;
    private final boolean lazy;

    protected EnumPolyStateful(Class<S> clazz, boolean lazy) {
        this(clazz, clazz.getEnumConstants(), lazy);
    }

    protected EnumPolyStateful(Class<S> clazz, @NotNull S[] states, boolean lazy) {
        this.stateMap = new EnumMap<>(clazz);
        this.states = Collections.newSetFromMap(new EnumMap<>(clazz));
        this.states.addAll(Arrays.asList(states));
        this.lazy = lazy;
        if (!lazy) {
            populateStates();
        }
    }

    protected void populateStates() {
        for (S state : this.states) {
            this.stateMap.computeIfAbsent(state, this::createInstance);
        }
    }

    protected abstract @Nullable T createInstance(@NotNull S state);

    @Override
    public @NotNull Set<@NotNull S> states() {
        return Collections.unmodifiableSet(this.states);
    }

    @Override
    public @NotNull Optional<@NotNull T> forState(@NotNull final S state) {
        final T value;
        if (this.lazy) {
            value = this.stateMap.computeIfAbsent(state, this::createInstance);
        } else {
            value = this.stateMap.get(state);
        }
        return Optional.ofNullable(value);
    }

    @Override
    public @NotNull Map<@NotNull S, @NotNull T> asStateMap() {
        return Collections.unmodifiableMap(this.stateMap);
    }
}
