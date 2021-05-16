package com.github.md5sha256.addictiveexperience.api.drugs;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface DrugPlantData {

    @NotNull IDrug drug();

    long startTimeEpochMillis();

    default long growthTimeElapsed(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(growthTimeElapsedMillis(), timeUnit);
    }

    default long growthTimeElapsedMillis() {
        long now = System.currentTimeMillis();
        return now - startTimeEpochMillis();
    }

}
