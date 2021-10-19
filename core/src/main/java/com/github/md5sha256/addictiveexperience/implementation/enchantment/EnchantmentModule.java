package com.github.md5sha256.addictiveexperience.implementation.enchantment;

import com.github.md5sha256.addictiveexperience.api.enchantment.IEnchantmentHandler;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class EnchantmentModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CustomEnchantmentResolver.class).to(PDCResolver.class).in(Singleton.class);
        bind(IEnchantmentHandler.class).to(SimpleEnchantmentHandler.class).asEagerSingleton();
    }
}
