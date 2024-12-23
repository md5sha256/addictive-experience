package io.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.google.common.base.Preconditions;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class DrugPlantMetaBuilder {

    private long growthTimeMillis;
    private double seedDropProbability;
    private int seedDropAmount;
    private double harvestProbability;
    private int harvestAmount;
    private IDrugComponent seed;
    private IDrugComponent result;

    public DrugPlantMetaBuilder() {
    }

    public DrugPlantMetaBuilder(@NotNull DrugPlantMetaBuilder builder) {
        this.growthTimeMillis = builder.growthTimeMillis;
        this.seedDropProbability = builder.seedDropProbability;
        this.seedDropAmount = builder.seedDropAmount;
        this.harvestProbability = builder.harvestProbability;
        this.harvestAmount = builder.harvestAmount;
        this.seed = builder.seed;
        this.result = builder.result;
    }

    public DrugPlantMetaBuilder(@NotNull DrugPlantMeta meta) {
        this.growthTimeMillis = meta.growthTimeMillis();
        this.seedDropProbability = meta.seedDropProbability();
        this.seedDropAmount = meta.seedDropAmount();
        this.harvestProbability = meta.harvestSuccessProbability();
        this.harvestAmount = meta.harvestAmount();
        this.seed = meta.seed().orElse(null);
        this.result = meta.result();
    }

    public DrugPlantMetaBuilder growthTimeMillis(long growthTimeMillis) {
        this.growthTimeMillis = growthTimeMillis;
        return this;
    }

    public DrugPlantMetaBuilder growthTime(long growthTime, @NotNull TimeUnit timeUnit) {
        this.growthTimeMillis = timeUnit.toMillis(growthTime);
        return this;
    }

    public DrugPlantMetaBuilder seedDropProbability(double seedDropProbability) {
        this.seedDropProbability = seedDropProbability;
        return this;
    }

    public DrugPlantMetaBuilder seedDropAmount(int seedDropAmount) {
        this.seedDropAmount = seedDropAmount;
        return this;
    }

    public DrugPlantMetaBuilder harvestProbability(double harvestProbability) {
        this.harvestProbability = harvestProbability;
        return this;
    }

    public DrugPlantMetaBuilder harvestAmount(int harvestAmount) {
        this.harvestAmount = harvestAmount;
        return this;
    }

    public DrugPlantMetaBuilder seed(IDrugComponent seed) {
        this.seed = seed;
        return this;
    }

    public DrugPlantMetaBuilder result(@NotNull IDrugComponent result) {
        this.result = result;
        return this;
    }

    private void validate() {
        Preconditions.checkArgument(growthTimeMillis > 0, "Invalid growth time: ", growthTimeMillis);
        Preconditions.checkArgument(seedDropAmount >= 0, "Invalid seed drop amount: ", seedDropAmount);
        Preconditions.checkArgument(seedDropProbability >= 0 && seedDropProbability <= 1,
                        "Invalid seed drop probability: ",
                        seedDropProbability);
        Preconditions.checkArgument(harvestProbability >= 0 && harvestProbability <= 1,
                        "Invalid harvest probability: ",
                        harvestProbability);
        Preconditions.checkArgument(harvestAmount >= 0, "Invalid harvest amount: ", harvestAmount);
        Preconditions.checkNotNull(result, "Drug cannot be null");
    }

    public DrugPlantMetaImpl build() {
        validate();
        return new DrugPlantMetaImpl(growthTimeMillis,
                                     seedDropProbability,
                                     seedDropAmount,
                                     harvestProbability,
                                     harvestAmount,
                                     seed,
                                     result);
    }

}
