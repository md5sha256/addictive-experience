package io.github.md5sha256.addictiveexperience.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Stateful<T, S> {

    @NotNull Set<@NotNull S> states();

    @NotNull Optional<@NotNull T> forState(@NotNull S state);

    @NotNull Map<@NotNull S, @NotNull T> asStateMap();

}
