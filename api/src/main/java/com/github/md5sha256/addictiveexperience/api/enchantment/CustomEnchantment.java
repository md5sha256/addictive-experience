package com.github.md5sha256.addictiveexperience.api.enchantment;

import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface CustomEnchantment extends Keyed {

    @NotNull String name();

    @NotNull Component displayName();

    void applyTo(@NotNull Entity entity);

    void removeFrom(@NotNull Entity entity);

    long duration(@NotNull TimeUnit timeUnit);

    default long durationMillis() {
        return duration(TimeUnit.MILLISECONDS);
    }

}
