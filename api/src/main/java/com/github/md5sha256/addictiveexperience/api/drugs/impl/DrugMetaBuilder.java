package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public final class DrugMetaBuilder {

    private Collection<PotionEffect> effects;
    private boolean enabled;
    private ISlurEffect effect;
    private long slurDurationMillis;
    private int overdoseThreshold;

    public DrugMetaBuilder() {
    }

    public DrugMetaBuilder(@NotNull DrugMetaBuilder builder) {
        this.enabled = builder.enabled;
        this.effect = builder.effect;
        if (builder.effects != null) {
            this.effects = new HashSet<>(builder.effects);
        }
        this.slurDurationMillis = builder.slurDurationMillis;
        this.overdoseThreshold = builder.overdoseThreshold;
    }

    public DrugMetaBuilder(@NotNull DrugMeta meta) {
        this.effects = new HashSet<>(meta.effects());
        this.enabled = meta.drugEnabled();
        this.effect = meta.slurEffect().orElse(null);
        this.slurDurationMillis = meta.slurDurationMillis();
        this.overdoseThreshold = meta.overdoseThreshold();
    }

    public DrugMetaBuilder effects(@NotNull PotionEffect... effects) {
        return effects(Arrays.asList(Objects.requireNonNull(effects)));
    }

    public DrugMetaBuilder effects(@NotNull Collection<PotionEffect> effects) {
        this.effects = Objects.requireNonNull(effects);
        return this;
    }

    public DrugMetaBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public DrugMetaBuilder effect(@Nullable ISlurEffect effect) {
        this.effect = effect;
        return this;
    }

    public DrugMetaBuilder slurDurationMillis(long slurDurationMillis) {
        this.slurDurationMillis = slurDurationMillis;
        return this;
    }

    public DrugMetaBuilder overdoseThreshold(int overdoseThreshold) {
        this.overdoseThreshold = overdoseThreshold;
        return this;
    }

    private void validate() {
        if (this.effects == null) {
            this.effects = Collections.emptySet();
        }
        if (this.slurDurationMillis < 0) {
            throw new IllegalArgumentException("Invalid slur duration: " + this.slurDurationMillis);
        }
        if (this.overdoseThreshold < 1) {
            throw new IllegalArgumentException("Invalid overdose threshold: " + this.overdoseThreshold);
        }
    }

    public DrugMeta build() {
        validate();
        return new DrugMetaImpl(new HashSet<>(this.effects),
                                this.enabled,
                                this.effect,
                                this.slurDurationMillis,
                                this.overdoseThreshold);
    }
}
