package com.github.md5sha256.addictiveexperience.configuration;

import org.jetbrains.annotations.NotNull;

public interface DrugFunConfiguration {

    @NotNull MessageRegistry messages();

    @NotNull DrugConfiguration drugConfiguration();

    @NotNull ShopConfiguration shopConfiguration();

}
