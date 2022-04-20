package io.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface MessageRegistry {

    @NotNull Message message(@NotNull MessageKey key);

    @NotNull Message message(@NotNull MessageKey key, @NotNull Component def);

    void message(@NotNull Message message);

}
