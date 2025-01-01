package io.github.md5sha256.addictiveexperience.implementation.plant;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PlantModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlantListener.class).asEagerSingleton();
        bind(PlantDataResolver.class).to(SQLResolver.class).asEagerSingleton();
    }
}
