package com.github.md5sha256.addictiveexperience.api.slur;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface SlurEffectState {

    void registerSlur(@NotNull UUID player,
                      @NotNull ISlurEffect effect,
                      long duration,
                      @NotNull TimeUnit timeUnit);

    void registerSlur(@NotNull UUID player, @NotNull IDrug drug);

    void clearSlur(@NotNull UUID player);

    void update();

    void update(@NotNull UUID player);

    @NotNull Optional<@NotNull ISlurEffect> currentSlurEffect(@NotNull UUID player);

    long remainingTimeMillis(@NotNull UUID player);

    long remainingTime(@NotNull UUID player, @NotNull TimeUnit timeUnit);

}
