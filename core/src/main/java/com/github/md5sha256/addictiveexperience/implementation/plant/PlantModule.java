package com.github.md5sha256.addictiveexperience.implementation.plant;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PlantModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlantDataResolver.class).to(PDCResolver.class).in(Singleton.class);
        bind(PlantListener.class).asEagerSingleton();
    }
}
