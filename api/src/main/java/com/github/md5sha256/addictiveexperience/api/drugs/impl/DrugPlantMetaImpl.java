package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class DrugPlantMetaImpl implements DrugPlantMeta {

    private final long growthTimeMillis;
    private final double seedDropProbability;
    private final int seedDropAmount;
    private final double harvestProbability;
    private final int harvestAmount;

    private final @Nullable IDrugComponent seed;
    private final IDrugComponent result;

    DrugPlantMetaImpl(@Range(from = 0, to = Long.MAX_VALUE) long growthTimeMillis,
                      @Range(from = 0, to = 1) double seedDropProbability,
                      @Range(from = 0, to = Integer.MAX_VALUE) int seedDropAmount,
                      @Range(from = 0, to = 1) double harvestProbability,
                      @Range(from = 0, to = Integer.MAX_VALUE) int harvestAmount,
                      @Nullable IDrugComponent seed,
                      @NotNull IDrugComponent result
    ) {
        this.growthTimeMillis = growthTimeMillis;
        this.seedDropAmount = seedDropAmount;
        this.seedDropProbability = seedDropProbability;
        this.harvestProbability = harvestProbability;
        this.harvestAmount = harvestAmount;
        this.seed = seed;
        this.result = result;
    }

    @Override
    public long growthTime(@NotNull final TimeUnit timeUnit) {
        return timeUnit.convert(this.growthTimeMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public long growthTimeMillis() {
        return this.growthTimeMillis;
    }

    @Override
    public double seedDropProbability() {
        return this.seedDropProbability;
    }

    @Override
    public int seedDropAmount() {
        return this.seedDropAmount;
    }

    @Override
    public @NotNull Optional<@NotNull IDrugComponent> seed() {
        return Optional.ofNullable(this.seed);
    }

    @Override
    public @NotNull IDrugComponent result() {
        return this.result;
    }

    @Override
    public double harvestSuccessProbability() {
        return this.harvestProbability;
    }

    @Override
    public int harvestAmount() {
        return this.harvestAmount;
    }

    @Override
    public String toString() {
        return "DrugPlantMetaImpl{" +
                "growthTimeMillis=" + growthTimeMillis +
                ", seedDropProbability=" + seedDropProbability +
                ", seedDropAmount=" + seedDropAmount +
                ", harvestProbability=" + harvestProbability +
                ", harvestAmount=" + harvestAmount +
                ", seed=" + seed +
                ", result=" + result.identifierName() +
                '}';
    }
}
