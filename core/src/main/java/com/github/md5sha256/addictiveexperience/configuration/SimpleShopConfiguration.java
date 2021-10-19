package com.github.md5sha256.addictiveexperience.configuration;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
public class SimpleShopConfiguration implements ShopConfiguration {

    @Setting
    private Map<Key, Double> unitPrices = new HashMap<>();

    @Override
    public double unitPrice(@NotNull final IDrugComponent drugComponent) {
        return this.unitPrices.getOrDefault(drugComponent.key(), 1D);
    }

}
