package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

public final class DrugPlantDataImpl implements DrugPlantData {

    private final IDrug drug;
    private final long startTimeEpochMilli;

    DrugPlantDataImpl(@NotNull IDrug drug, long startTimeEpochMilli) {
        this.drug = drug;
        this.startTimeEpochMilli = startTimeEpochMilli;
    }

    @Override
    public @NotNull IDrug drug() {
        return this.drug;
    }

    @Override
    public long startTimeEpochMillis() {
        return this.startTimeEpochMilli;
    }

}
