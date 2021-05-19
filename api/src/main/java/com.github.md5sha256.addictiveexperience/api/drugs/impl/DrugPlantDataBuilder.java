package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public final class DrugPlantDataBuilder {

    private IDrug drug;
    private long startTimeEpochMilli;

    public DrugPlantDataBuilder() {

    }

    public DrugPlantDataBuilder(@NotNull DrugPlantData data) {
        this.drug = data.drug();
        this.startTimeEpochMilli = data.startTimeEpochMillis();
    }

    public DrugPlantDataBuilder(@NotNull DrugPlantDataBuilder builder) {
        this.drug = builder.drug;
        this.startTimeEpochMilli = builder.startTimeEpochMilli;
    }

    public @NotNull DrugPlantDataBuilder drug(@NotNull IDrug drug) {
        this.drug = drug;
        return this;
    }

    public @NotNull DrugPlantDataBuilder startTimeEpochMilli(long startTimeEpochMilli) {
        this.startTimeEpochMilli = startTimeEpochMilli;
        return this;
    }

    private void validate() {
        Validate.notNull(this.drug, "Drug cannot be null");
        Validate.isTrue(this.startTimeEpochMilli >= 0, "Invalid start time!");
    }

    public @NotNull DrugPlantDataImpl build() {
        return new DrugPlantDataImpl(this.drug, this.startTimeEpochMilli);
    }
}
