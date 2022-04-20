package io.github.md5sha256.addictiveexperience.api.effect;

import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface CustomEffect extends Keyed {

    @NotNull String name();

    @NotNull Component displayName();

    void applyTo(@NotNull LivingEntity entity);

    void removeFrom(@NotNull LivingEntity entity);

    long duration(@NotNull TimeUnit timeUnit);

    default long durationMillis() {
        return duration(TimeUnit.MILLISECONDS);
    }

}
