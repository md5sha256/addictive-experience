package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.IStopwatch;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class DrugPlantDataImpl implements DrugPlantData {

    private final DrugPlantMeta meta;
    private final long startTimeEpochMilli;
    private final VariableStopwatch stopwatch;
    private final BlockPosition position;

    DrugPlantDataImpl(@NotNull BlockPosition position,
                      @NotNull DrugPlantMeta meta,
                      long startTimeEpochMilli,
                      @NotNull IStopwatch stopwatch) {
        this.position = position;
        this.meta = meta;
        this.startTimeEpochMilli = startTimeEpochMilli;
        if (stopwatch instanceof final VariableStopwatch variableStopwatch) {
            this.stopwatch = Stopwatches.variableStopwatch(GuavaAdapter.ofStarted());
            this.stopwatch.setElapsedTime(variableStopwatch.elapsedNanos(), TimeUnit.NANOSECONDS);
        } else {
            this.stopwatch = Stopwatches.variableStopwatch(stopwatch).start();
        }
    }

    @Override
    public @NotNull DrugPlantMeta meta() {
        return this.meta;
    }

    @Override
    public @NotNull BlockPosition position() {
        return this.position;
    }

    @Override
    public long startTimeEpochMillis() {
        return this.startTimeEpochMilli;
    }

    @Override
    public long growthTimeElapsedMillis() {
        return this.stopwatch.elapsedMillis();
    }

    @Override
    public @NotNull VariableStopwatch elapsed() {
        return this.stopwatch;
    }

}
