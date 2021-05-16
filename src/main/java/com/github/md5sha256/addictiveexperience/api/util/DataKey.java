package com.github.md5sha256.addictiveexperience.api.util;

import com.github.md5sha256.addictiveexperience.implementation.DataKeyImpl;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

public interface DataKey<T> {

    static <T> @NotNull DataKey<T> of(@NotNull String name, @NotNull TypeToken<T> type) {
        return new DataKeyImpl<>(name, type);
    }

    static <T> @NotNull DataKey<T> of(@NotNull String name, @NotNull Class<T> type) {
        return of(name, TypeToken.get(type));
    }

    @NotNull String name();

    @NotNull TypeToken<T> type();

}
