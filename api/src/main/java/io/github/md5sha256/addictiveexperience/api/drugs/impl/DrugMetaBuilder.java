package io.github.md5sha256.addictiveexperience.api.drugs.impl;

import io.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import io.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class DrugMetaBuilder {

    private Set<PotionEffect> potionEffects;
    private Set<CustomEffect> customEffects;
    private boolean enabled;
    private ISlurEffect effect;
    private long slurDurationMillis;
    private int overdoseThreshold;

    public DrugMetaBuilder() {
    }

    public DrugMetaBuilder(@NotNull DrugMetaBuilder builder) {
        this.enabled = builder.enabled;
        this.effect = builder.effect;
        if (builder.potionEffects != null) {
            this.potionEffects = new HashSet<>(builder.potionEffects);
        }
        if (builder.customEffects != null) {
            this.customEffects = new HashSet<>(builder.customEffects);
        }
        this.slurDurationMillis = builder.slurDurationMillis;
        this.overdoseThreshold = builder.overdoseThreshold;
    }

    public DrugMetaBuilder(@NotNull DrugMeta meta) {
        this.potionEffects = new HashSet<>(meta.potionEffects());
        this.enabled = meta.drugEnabled();
        this.effect = meta.slurEffect().orElse(null);
        this.slurDurationMillis = meta.slurDurationMillis();
        this.overdoseThreshold = meta.overdoseThreshold();
    }

    public DrugMetaBuilder potionEffects(@NotNull PotionEffect @NotNull ... effects) {
        return potionEffects(Arrays.asList(Objects.requireNonNull(effects)));
    }

    public DrugMetaBuilder potionEffects(@NotNull Collection<PotionEffect> effects) {
        if (effects instanceof Set) {
            this.potionEffects = (Set<PotionEffect>) effects;
        } else {
            this.potionEffects = new HashSet<>(Objects.requireNonNull(effects));
        }
        return this;
    }

    public DrugMetaBuilder customEffects(@NotNull CustomEffect @NotNull ... effects) {
        return customEffects(Arrays.asList(effects));
    }

    public DrugMetaBuilder customEffects(@NotNull Collection<CustomEffect> effects) {
        if (effects instanceof Set) {
            this.customEffects = (Set<CustomEffect>) effects;
        } else {
            this.customEffects = new HashSet<>(Objects.requireNonNull(effects));
        }
        return this;
    }

    public DrugMetaBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public DrugMetaBuilder slurEffect(@Nullable ISlurEffect effect) {
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
        if (this.potionEffects == null) {
            this.potionEffects = Collections.emptySet();
        }
        if (this.customEffects == null) {
            this.customEffects = Collections.emptySet();
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
        return new DrugMetaImpl(Collections.unmodifiableSet(this.potionEffects),
                                Collections.unmodifiableSet(this.customEffects),
                                this.enabled,
                                this.effect,
                                this.slurDurationMillis,
                                this.overdoseThreshold);
    }
}
