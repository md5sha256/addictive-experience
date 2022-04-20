package io.github.md5sha256.addictiveexperience.api.drugs.impl;

import io.github.md5sha256.addictiveexperience.api.drugs.SmeltingMeta;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public final class SmeltingMetaBuilder {

    private float experienceGain;
    private int cookTimeTicks;
    private int smeltProductQuantity = 1;

    public SmeltingMetaBuilder() {
    }

    public SmeltingMetaBuilder(@NotNull SmeltingMeta meta) {
        this.experienceGain = meta.experienceGain();
        this.cookTimeTicks = meta.cookTimeTicks();
        this.smeltProductQuantity = meta.smeltProductQuantity();
    }

    public SmeltingMetaBuilder experienceGain(float experienceGain) {
        this.experienceGain = experienceGain;
        return this;
    }

    public SmeltingMetaBuilder cookTimeTicks(int cookTimeTicks) {
        this.cookTimeTicks = cookTimeTicks;
        return this;
    }

    public SmeltingMetaBuilder smeltProductQuantity(int productQuantity) {
        this.smeltProductQuantity = productQuantity;
        return this;
    }

    private void validate() throws IllegalArgumentException {
        Validate.isTrue(this.smeltProductQuantity > 0,
                        "Smelt product quantity must be greater than 0");
        Validate.isTrue(this.experienceGain >= 0f, "Experience gain must be positive");
        Validate.isTrue(this.cookTimeTicks >= 0, "Cook time in ticks must be greater than 1");
    }

    public SmeltingMetaImpl build() {
        validate();
        return new SmeltingMetaImpl(this.smeltProductQuantity,
                                    this.experienceGain,
                                    this.cookTimeTicks);
    }

    @Override
    public int hashCode() {
        int result = (experienceGain != +0.0f ? Float.floatToIntBits(experienceGain) : 0);
        result = 31 * result + cookTimeTicks;
        result = 31 * result + smeltProductQuantity;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmeltingMetaBuilder that = (SmeltingMetaBuilder) o;

        if (Float.compare(that.experienceGain, experienceGain) != 0) return false;
        if (cookTimeTicks != that.cookTimeTicks) return false;
        return smeltProductQuantity == that.smeltProductQuantity;
    }
}
