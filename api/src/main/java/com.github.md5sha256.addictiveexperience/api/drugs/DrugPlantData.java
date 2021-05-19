package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantDataBuilder;
import com.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.TimeUnit;

public interface DrugPlantData extends SimilarLike<DrugPlantData> {

    static @NotNull DrugPlantDataBuilder builder() {
        return new DrugPlantDataBuilder();
    }

    @NotNull IDrug drug();

    @Range(from = 0, to = Long.MAX_VALUE) long startTimeEpochMillis();

    default long growthTimeElapsed(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(growthTimeElapsedMillis(), timeUnit);
    }

    @Range(from = 0, to = Long.MAX_VALUE)
    default long growthTimeElapsedMillis() {
        long now = System.currentTimeMillis();
        return now - startTimeEpochMillis();
    }

    default @NotNull DrugPlantDataBuilder toBuilder() {
        return new DrugPlantDataBuilder(this);
    }

    @Override
    default boolean isSimilar(@NotNull DrugPlantData other) {
        return this.growthTimeElapsedMillis() == other.startTimeEpochMillis()
                && this.drug().equals(other.drug());
    }

}
