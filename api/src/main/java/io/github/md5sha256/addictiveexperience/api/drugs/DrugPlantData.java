package io.github.md5sha256.addictiveexperience.api.drugs;

import io.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantDataBuilder;
import io.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.TimeUnit;

public interface DrugPlantData extends SimilarLike<DrugPlantData> {

    static @NotNull DrugPlantDataBuilder builder() {
        return new DrugPlantDataBuilder();
    }

    @NotNull DrugPlantMeta meta();

    @NotNull BlockPosition position();

    @Range(from = 0, to = Long.MAX_VALUE) long startTimeEpochMillis();

    default long growthTimeElapsed(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(growthTimeElapsedMillis(), timeUnit);
    }

    long growthTimeElapsedMillis();

    default long remainingMillis() {
        return Math.max(meta().growthTimeMillis() - growthTimeElapsedMillis(), 0);
    }

    @NotNull VariableStopwatch elapsed();

    default @NotNull DrugPlantDataBuilder toBuilder() {
        return new DrugPlantDataBuilder(this);
    }

    @Override
    default boolean isSimilar(@NotNull DrugPlantData other) {
        return this.growthTimeElapsedMillis() == other.growthTimeElapsedMillis()
                && this.position().equals(other.position())
                && this.meta().isSimilar(other.meta());
    }

}
