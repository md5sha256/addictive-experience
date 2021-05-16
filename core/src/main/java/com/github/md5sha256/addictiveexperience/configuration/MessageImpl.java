package com.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class MessageImpl implements Message {

    private final MessageKey key;
    private final Component value;

    public MessageImpl(@NotNull MessageKey key, @NotNull Component component) {
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(component);
    }

    @Override
    public @NotNull MessageKey key() {
        return this.key;
    }

    @Override
    public @NotNull Component value() {
        return this.value;
    }


}
