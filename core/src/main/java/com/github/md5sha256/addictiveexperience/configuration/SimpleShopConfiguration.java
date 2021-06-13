package com.github.md5sha256.addictiveexperience.configuration;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import org.jetbrains.annotations.NotNull;

public class SimpleShopConfiguration implements ShopConfiguration {

    @Override
    public double unitPrice(@NotNull final IDrugComponent drugComponent) {
        return 1;
    }
}
