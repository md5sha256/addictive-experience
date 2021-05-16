package com.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Message {

    static @NotNull Message create(@NotNull MessageKey key, @NotNull Component value) {
        return new MessageImpl(key, value);
    }

    static @NotNull Message empty(@NotNull MessageKey key) {
        return create(key, Component.empty());
    }

    @NotNull MessageKey key();

    @NotNull Component value();

}
