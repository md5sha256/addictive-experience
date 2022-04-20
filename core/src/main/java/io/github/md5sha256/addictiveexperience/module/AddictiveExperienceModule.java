package io.github.md5sha256.addictiveexperience.module;

import io.github.md5sha256.addictiveexperience.api.AddictiveExperience;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import io.github.md5sha256.addictiveexperience.configuration.AddictiveExperienceConfiguration;
import io.github.md5sha256.addictiveexperience.configuration.ShopConfiguration;
import io.github.md5sha256.addictiveexperience.configuration.SimpleAddictiveConfiguration;
import io.github.md5sha256.addictiveexperience.configuration.SimpleShopConfiguration;
import io.github.md5sha256.addictiveexperience.implementation.DrugHandlerImpl;
import io.github.md5sha256.addictiveexperience.implementation.FormHandlerPowder;
import io.github.md5sha256.addictiveexperience.implementation.FormHandlerSyringe;
import io.github.md5sha256.addictiveexperience.implementation.SlurEffectStateImpl;
import io.github.md5sha256.addictiveexperience.implementation.commands.AddictiveExperienceCommandHandler;
import io.github.md5sha256.addictiveexperience.implementation.drugs.SimpleDrugRegistry;
import io.github.md5sha256.addictiveexperience.implementation.effects.EffectsModule;
import io.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
import io.github.md5sha256.addictiveexperience.implementation.plant.PlantHandlerImpl;
import io.github.md5sha256.addictiveexperience.implementation.plant.PlantModule;
import io.github.md5sha256.addictiveexperience.implementation.shop.DrugShopUI;
import com.github.md5sha256.spigotutils.module.BukkitPlatformModule;
import com.google.inject.AbstractModule;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

public final class AddictiveExperienceModule extends AbstractModule {

    private final AddictiveExperience api;
    private final JavaPlugin plugin;

    public AddictiveExperienceModule(@NotNull AddictiveExperience api, @NotNull JavaPlugin plugin) {
        this.api = api;
        this.plugin = plugin;
    }

    @Override
    protected void configure() {

        // Platform stuff

        install(new BukkitPlatformModule());
        bind(Plugin.class).toInstance(this.plugin);
        bind(JavaPlugin.class).toInstance(this.plugin);
        install(new DependencyModule());
        bind(AddictiveExperienceConfiguration.class).to(SimpleAddictiveConfiguration.class);
        bind(ShopConfiguration.class).to(SimpleShopConfiguration.class).asEagerSingleton();
        bind(DrugRegistry.class).to(SimpleDrugRegistry.class).asEagerSingleton();

        // Enchantments
        install(new EffectsModule());

        // Drugs Implementation
        install(new DrugsModule());
        // Implementation
        install(new PlantModule());
        bind(DrugHandler.class).to(DrugHandlerImpl.class).asEagerSingleton();
        bind(IPlantHandler.class).to(PlantHandlerImpl.class).asEagerSingleton();
        bind(SlurEffectState.class).to(SlurEffectStateImpl.class).asEagerSingleton();

        // Core
        bind(DrugShopUI.class).asEagerSingleton();
        bind(AddictiveExperienceCommandHandler.class).asEagerSingleton();
        bind(FormHandlerPowder.class).asEagerSingleton();
        bind(FormHandlerSyringe.class).asEagerSingleton();

        // API bindings
        bind(IDrugForms.class).to(DrugForms.class).in(Singleton.class);
        bind(AddictiveExperience.class).toInstance(this.api);
    }
}
