package com.github.md5sha256.addictiveexperience.api.util;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface KeyRegistry {

    @NotNull Set<String> values(@NotNull String namespace);

    @NotNull Set<String> keysForValue(@NotNull String value);

    @NotNull Set<String> keys();

    void register(@NotNull Key key);

}
