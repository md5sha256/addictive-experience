package io.github.md5sha256.addictiveexperience.api.drugs;

import io.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantMetaBuilder;
import io.github.md5sha256.addictiveexperience.api.util.DataKey;
import io.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface DrugPlantMeta extends SimilarLike<DrugPlantMeta> {

    @NotNull DataKey<DrugPlantMeta> KEY = DataKey.of("plant-meta", DrugPlantMeta.class);

    static @NotNull DrugPlantMetaBuilder builder() {
        return new DrugPlantMetaBuilder();
    }

    static @NotNull DrugPlantMeta defaultMeta(@NotNull IDrugComponent result) {
        return defaultMeta(result, null);
    }

    static @NotNull DrugPlantMeta defaultMeta(@NotNull IDrugComponent result, @Nullable IDrugComponent seed) {
        return DrugPlantMeta.builder()
                            .result(result)
                            .seed(seed)
                            .harvestAmount(1)
                            .growthTime(10, TimeUnit.SECONDS)
                            .seedDropProbability(1)
                            .seedDropAmount(1)
                            .harvestProbability(1)
                            .build();
    }

    long growthTime(@NotNull TimeUnit timeUnit);

    long growthTimeMillis();

    double seedDropProbability();

    int seedDropAmount();

    @NotNull Optional<@NotNull IDrugComponent> seed();

    @NotNull IDrugComponent result();

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
                && this.result().equals(other.result());
    }

}
