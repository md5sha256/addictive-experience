package io.github.md5sha256.addictiveexperience.api.drugs;

import io.github.md5sha256.addictiveexperience.api.drugs.impl.SmeltingMetaBuilder;
import org.jetbrains.annotations.NotNull;

public interface SmeltingMeta {

    static @NotNull SmeltingMetaBuilder builder() {
        return new SmeltingMetaBuilder();
    }

    int smeltProductQuantity();

    float experienceGain();

    int cookTimeTicks();

    default @NotNull SmeltingMetaBuilder toBuilder() {
        return new SmeltingMetaBuilder(this);
    }

}
