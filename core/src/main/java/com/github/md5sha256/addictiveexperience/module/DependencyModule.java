package com.github.md5sha256.addictiveexperience.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.internal.ProviderMethodsModule;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        install(ProviderMethodsModule.forModule(this));
    }

    @Provides
    public @Nullable Economy provideEconomy(@NotNull ServicesManager servicesManager) {
        final RegisteredServiceProvider<Economy> rsp = servicesManager
                .getRegistration(Economy.class);
        if (rsp != null) {
            return rsp.getProvider();
        }
        return null;
    }

    @Provides
    public @Nullable Permission providePermission(@NotNull ServicesManager servicesManager) {
        final RegisteredServiceProvider<Permission> rsp = servicesManager
                .getRegistration(Permission.class);
        if (rsp != null) {
            return rsp.getProvider();
        }
        return null;
    }


}
