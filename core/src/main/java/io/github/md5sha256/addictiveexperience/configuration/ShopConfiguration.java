package io.github.md5sha256.addictiveexperience.configuration;

import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import org.jetbrains.annotations.NotNull;

public interface ShopConfiguration {

    double unitPrice(@NotNull IDrugComponent drugComponent);

    default double price(@NotNull IDrugComponent drugComponent, int amount) {
        return unitPrice(drugComponent) * amount;
    }

}
