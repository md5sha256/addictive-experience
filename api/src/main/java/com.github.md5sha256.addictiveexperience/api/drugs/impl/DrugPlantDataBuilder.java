package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.IStopwatch;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public final class DrugPlantDataBuilder {

    private DrugPlantMeta meta;
    private long startTimeEpochMilli;
    private IStopwatch stopwatch;
    private BlockPosition position;

    public DrugPlantDataBuilder() {

    }

    public DrugPlantDataBuilder(@NotNull DrugPlantData data) {
        this.meta = data.meta();
        this.stopwatch = data.elapsed();
        this.position = data.position();
        this.startTimeEpochMilli = data.startTimeEpochMillis();
    }

    public DrugPlantDataBuilder(@NotNull DrugPlantDataBuilder builder) {
        this.meta = builder.meta;
        this.position = builder.position;
        this.stopwatch = builder.stopwatch;
        this.startTimeEpochMilli = builder.startTimeEpochMilli;
    }

    public @NotNull DrugPlantDataBuilder meta(@NotNull DrugPlantMeta meta) {
        this.meta = meta;
        return this;
    }

    public @NotNull DrugPlantDataBuilder startTimeEpochMilli(long startTimeEpochMilli) {
        this.startTimeEpochMilli = startTimeEpochMilli;
        return this;
    }

    public @NotNull DrugPlantDataBuilder elapsed(@NotNull IStopwatch stopwatch) {
        this.stopwatch = stopwatch;
        return this;
    }

    public @NotNull DrugPlantDataBuilder position(@NotNull BlockPosition position) {
        this.position = position;
        return this;
    }

    private void validate() {
        Validate.notNull(this.meta, "Meta cannot be null");
        Validate.notNull(this.position, "Position cannot be null");
        Validate.isTrue(this.startTimeEpochMilli >= 0, "Invalid start time");
    }

    public @NotNull DrugPlantDataImpl build() {
        validate();
        if (this.stopwatch == null) {
            this.stopwatch = GuavaAdapter.ofStarted();
        }
        return new DrugPlantDataImpl(this.position,
                                     this.meta,
                                     this.startTimeEpochMilli,
                                     this.stopwatch);
    }
}
