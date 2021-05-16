package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.util.DataKey;
import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import com.github.md5sha256.addictiveexperience.implementation.DrugMetaBuilder;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface DrugMeta {

    @NotNull DataKey<DrugMeta> KEY = DataKey.of("DrugMeta", DrugMeta.class);

    @NotNull DrugMeta DEFAULT = builder()
            .enabled(false)
            .effect(null)
            .effects(Collections.emptySet())
            .overdoseThreshold(1)
            .slurDurationMillis(0)
            .build();


    static @NotNull DrugMetaBuilder builder() {
        return new DrugMetaBuilder();
    }

    boolean drugEnabled();

    @NotNull Optional<ISlurEffect> effect();

    long slurDuration(@NotNull TimeUnit timeUnit);

    default long slurDurationMillis() {
        return slurDuration(TimeUnit.MILLISECONDS);
    }

    @NotNull Collection<@NotNull PotionEffect> effects();

    int overdoseThreshold();

}
