package com.github.md5sha256.addictiveexperience.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class PolyStateful<T, S> implements Stateful<T, S> {

    private final Map<S, T> stateMap;
    private final Set<S> states;
    private final boolean lazy;

    protected PolyStateful(@NotNull Collection<S> states, boolean lazy) {
        this.states = new HashSet<>(states);
        this.stateMap = new HashMap<>(states.size());
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
