package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugMetaBuilder;
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

    long slurDurationMillis();

    @NotNull Set<@NotNull PotionEffect> effects();

    int overdoseThreshold();

    default @NotNull DrugMetaBuilder toBuilder() {
        return new DrugMetaBuilder(this);
    }

    default boolean isSimilar(@NotNull DrugMeta other) {
        return other == this || (other.drugEnabled() == this.drugEnabled()
                && this.slurDurationMillis() == other.slurDurationMillis()
                && this.overdoseThreshold() == other.overdoseThreshold()
                && this.effect().equals(other.effect())
                && CollectionComparison.haveIdenticalElements(this.effects(), other.effects()));
    }

}
