package com.github.md5sha256.addictiveexperience.configuration;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.NodePath;

@FunctionalInterface
public interface MessageKey {

    @NotNull String keyName();

    default @NotNull NodePath path() {
        return NodePath.path(keyName());
    }

}
