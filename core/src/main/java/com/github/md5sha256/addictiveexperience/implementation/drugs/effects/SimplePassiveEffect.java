package com.github.md5sha256.addictiveexperience.implementation.drugs.effects;

import com.github.md5sha256.addictiveexperience.api.drugs.PassiveEffect;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class SimplePassiveEffect implements PassiveEffect {

    private final Key key;
    private final String name;
    private final Component displayName;

    protected SimplePassiveEffect(
            @NotNull Key key,
            @NotNull String name,
            @NotNull Component displayName
    ) {
        this.key = key;
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public @NotNull String name() {
        return this.name;
    }

    public @NotNull Component displayName() {
        return this.displayName;
    }

}
