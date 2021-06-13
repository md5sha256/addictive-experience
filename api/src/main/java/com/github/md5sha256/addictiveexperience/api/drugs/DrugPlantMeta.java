package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantMetaBuilder;
import com.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface DrugPlantMeta extends SimilarLike<DrugPlantMeta> {

    static @NotNull DrugPlantMetaBuilder builder() {
        return new DrugPlantMetaBuilder();
    }

    long growthTime(@NotNull TimeUnit timeUnit);

    long growthTimeMillis();

    double seedDropProbability();

    int seedDropAmount();

    @NotNull Optional<@NotNull IDrugComponent> seed();

    @NotNull IDrug drug();

    double harvestSuccessProbability();

    int harvestAmount();

    default DrugPlantMetaBuilder toBuilder() {
        return new DrugPlantMetaBuilder(this);
    }

    default boolean isSimilar(@NotNull DrugPlantMeta other) {
        return this.harvestAmount() == other.harvestAmount()
                && this.seedDropAmount() == other.seedDropAmount()
                && this.growthTimeMillis() == other.growthTimeMillis()
                && this.seedDropProbability() == other.seedDropProbability()
                && this.harvestSuccessProbability() == other.harvestSuccessProbability()
                && this.seed().equals(other.seed())
                && this.drug().equals(other.drug());
    }

}
