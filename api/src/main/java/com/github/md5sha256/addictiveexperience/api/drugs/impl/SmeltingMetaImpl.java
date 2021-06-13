package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.SmeltingMeta;

public final class SmeltingMetaImpl implements SmeltingMeta {

    private final int smeltProductQuantity;

    private final float experienceGain;

    private final int cookTimeTicks;

    SmeltingMetaImpl(int productQuantity, float experienceGain, int cookTimeTicks) {
        this.smeltProductQuantity = productQuantity;
        this.experienceGain = experienceGain;
        this.cookTimeTicks = cookTimeTicks;
    }

    @Override
    public int smeltProductQuantity() {
        return this.smeltProductQuantity;
    }

    @Override
    public float experienceGain() {
        return this.experienceGain;
    }

    @Override
    public int cookTimeTicks() {
        return this.cookTimeTicks;
    }


}
