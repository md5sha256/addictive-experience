package io.github.md5sha256.addictiveexperience.configuration;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface AddictiveExperienceConfiguration {

    @NotNull MessageRegistry messages();

    @NotNull DrugConfiguration drugConfiguration();

    @NotNull ShopConfiguration shopConfiguration();

    @NotNull Component slurFormat();

}
