package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.implementation.DrugPlantMetaBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface DrugPlantMeta {

    static @NotNull DrugPlantMetaBuilder builder() {
        return new DrugPlantMetaBuilder();
    }

    long growthTime(@NotNull TimeUnit timeUnit);

    default long growthTimeMillis() {
        return growthTime(TimeUnit.MILLISECONDS);
    }

    double seedDropProbability();

    int seedDropAmount();

    @NotNull Optional<@NotNull IDrugComponent> seed();

    double harvestSuccessProbability();

    int harvestAmount();

    default DrugPlantMetaBuilder toBuilder() {
        return new DrugPlantMetaBuilder(this);
    }

}
