package com.github.md5sha256.addictiveexperience.implementation.plant;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PlantModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlantListener.class).asEagerSingleton();
        install(new FactoryModuleBuilder().implement(PlantDataResolver.class, PDCResolver.class)
                .build(PlantDataResolverFactory.class));
    }
}
