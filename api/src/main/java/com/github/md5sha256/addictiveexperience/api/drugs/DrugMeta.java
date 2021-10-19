package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugMetaBuilder;
import com.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import com.github.md5sha256.addictiveexperience.api.util.CollectionComparison;
import com.github.md5sha256.addictiveexperience.api.util.DataKey;
import com.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface DrugMeta extends SimilarLike<DrugMeta> {

    @NotNull DataKey<DrugMeta> KEY = DataKey.of("DrugMeta", DrugMeta.class);

    @NotNull DrugMeta DEFAULT = builder()
            .enabled(true)
            .potionEffects(Collections.emptySet())
            .overdoseThreshold(1)
            .slurEffect(null)
            .slurDurationMillis(TimeUnit.MINUTES.toMillis(5))
            .build();


    static @NotNull DrugMetaBuilder builder() {
        return new DrugMetaBuilder();
    }

    boolean drugEnabled();

    @NotNull Optional<ISlurEffect> slurEffect();

    long slurDuration(@NotNull TimeUnit timeUnit);

    long slurDurationMillis();

    @NotNull Set<@NotNull PotionEffect> potionEffects();

    @NotNull Set<@NotNull CustomEffect> customEffects();

    int overdoseThreshold();

    default @NotNull DrugMetaBuilder toBuilder() {
        return new DrugMetaBuilder(this);
    }

    default boolean isSimilar(@NotNull DrugMeta other) {
        return other == this || (other.drugEnabled() == this.drugEnabled()
                && this.slurDurationMillis() == other.slurDurationMillis()
                && this.overdoseThreshold() == other.overdoseThreshold()
                && this.slurEffect().equals(other.slurEffect())
                && CollectionComparison.haveIdenticalElements(this.potionEffects(), other.potionEffects()))
                && CollectionComparison.haveIdenticalElements(this.customEffects(), other.customEffects());
    }

}
