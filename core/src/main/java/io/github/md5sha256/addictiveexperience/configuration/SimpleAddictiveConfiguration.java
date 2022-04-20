package io.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class SimpleAddictiveConfiguration implements AddictiveExperienceConfiguration {

    private final MessageRegistry messages = new MessageRegistryImpl();
    private final DrugConfiguration drugConfiguration = new SimpleDrugConfiguration();
    private final ShopConfiguration shopConfiguration = new SimpleShopConfiguration();
    private final Component slurFormat = Component.text("slur format");

    @Override
    public @NotNull MessageRegistry messages() {
        return this.messages;
    }

    @Override
    public @NotNull DrugConfiguration drugConfiguration() {
        return this.drugConfiguration;
    }

    @Override
    public @NotNull ShopConfiguration shopConfiguration() {
        return this.shopConfiguration;
    }

    @Override
    public @NotNull Component slurFormat() {
        return this.slurFormat;
    }
}
