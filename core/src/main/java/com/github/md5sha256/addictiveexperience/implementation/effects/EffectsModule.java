package com.github.md5sha256.addictiveexperience.implementation.effects;

import com.github.md5sha256.addictiveexperience.api.effect.IEffectHandler;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class EffectsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CustomEffectResolver.class).to(PDCResolver.class).in(Singleton.class);
        bind(IEffectHandler.class).to(SimpleEffectHandler.class).asEagerSingleton();
    }
}
