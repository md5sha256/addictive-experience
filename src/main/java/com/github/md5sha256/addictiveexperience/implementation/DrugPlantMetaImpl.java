package com.github.md5sha256.addictiveexperience.implementation;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class DrugPlantMetaImpl implements DrugPlantMeta {

    private final long growthTimeMillis;
    private final double seedDropProbability;
    private final int seedDropAmount;
    private final double harvestProbability;
    private final int harvestAmount;

    private final @Nullable IDrugComponent seed;

    DrugPlantMetaImpl(@Range(from = 0, to = Long.MAX_VALUE) long growthTimeMillis,
                 @Range(from = 0, to = 1) double seedDropProbability,
                 @Range(from = 0, to = Integer.MAX_VALUE) int seedDropAmount,
                 @Range(from = 0, to = 1) double harvestProbability,
                 @Range(from = 0, to = Integer.MAX_VALUE) int harvestAmount,
                 @Nullable IDrugComponent seed
    ) {
        this.growthTimeMillis = growthTimeMillis;
        this.seedDropAmount = seedDropAmount;
        this.seedDropProbability = seedDropProbability;
        this.harvestProbability = harvestProbability;
        this.harvestAmount = harvestAmount;
        this.seed = seed;
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
    public double harvestSuccessProbability() {
        return this.harvestProbability;
    }

    @Override
    public int harvestAmount() {
        return this.harvestAmount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugPlantMetaImpl that = (DrugPlantMetaImpl) o;

        if (growthTimeMillis != that.growthTimeMillis) return false;
        if (Double.compare(that.seedDropProbability, seedDropProbability) != 0) return false;
        if (seedDropAmount != that.seedDropAmount) return false;
        if (Double.compare(that.harvestProbability, harvestProbability) != 0) return false;
        if (harvestAmount != that.harvestAmount) return false;
        return Objects.equals(seed, that.seed);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (growthTimeMillis ^ (growthTimeMillis >>> 32));
        temp = Double.doubleToLongBits(seedDropProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + seedDropAmount;
        temp = Double.doubleToLongBits(harvestProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + harvestAmount;
        result = 31 * result + (seed != null ? seed.hashCode() : 0);
        return result;
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
                '}';
    }
}
