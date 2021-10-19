package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DrugMetaImpl implements DrugMeta {

    private final Set<PotionEffect> potionEffects;
    private final Set<CustomEffect> customEffects;
    private final boolean enabled;
    private final ISlurEffect slurEffect;
    private final long slurDurationMillis;
    private final int overdoseThreshold;

    DrugMetaImpl(@NotNull Set<@NotNull PotionEffect> potionEffects,
                 @NotNull Set<@NotNull CustomEffect> customEffects,
                 boolean enabled,
                 @Nullable ISlurEffect slurEffect,
                 long slurDurationMillis,
                 int overdoseThreshold) {
        this.potionEffects = potionEffects;
        this.customEffects = customEffects;
        this.enabled = enabled;
        this.slurEffect = slurEffect;
        this.slurDurationMillis = slurDurationMillis;
        this.overdoseThreshold = overdoseThreshold;
    }

    @Override
    public boolean drugEnabled() {
        return this.enabled;
    }

    @Override
    public @NotNull Optional<@NotNull ISlurEffect> slurEffect() {
        return Optional.ofNullable(this.slurEffect);
    }

    @Override
    public long slurDuration(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(this.slurDurationMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public long slurDurationMillis() {
        return this.slurDurationMillis;
    }

    @Override
    public @NotNull Set<@NotNull PotionEffect> potionEffects() {
        return this.potionEffects;
    }

    @Override
    public @NotNull Set<@NotNull CustomEffect> customEffects() {
        return this.customEffects;
    }

    @Override
    public int overdoseThreshold() {
        return this.overdoseThreshold;
    }
}
