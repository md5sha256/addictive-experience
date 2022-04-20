package io.github.md5sha256.addictiveexperience.api.effect;

import io.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface EffectData extends SimilarLike<EffectData> {

    @NotNull VariableStopwatch elapsed();

    long durationMillis();

    long duration(@NotNull TimeUnit timeUnit);

    long remainingDurationMillis();

    long remainingDuration(@NotNull TimeUnit timeUnit);

    @Override
    default boolean isSimilar(@NotNull EffectData other) {
        return this.durationMillis() == other.durationMillis()
                && this.remainingDurationMillis() == other.remainingDurationMillis();
    }
}
