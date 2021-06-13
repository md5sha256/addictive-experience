package com.github.md5sha256.addictiveexperience;

import com.github.md5sha256.addictiveexperience.api.AddictiveExperience;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.github.md5sha256.addictiveexperience.configuration.AddictiveExperienceConfiguration;
import com.github.md5sha256.addictiveexperience.implementation.plant.PlantHandlerImpl;
import com.github.md5sha256.addictiveexperience.module.AddictiveExperienceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AddictiveExperiencePlugin extends JavaPlugin implements AddictiveExperience {

    private Injector injector;
    private DrugRegistry drugRegistry;
    private IPlantHandler plantHandler;
    private IDrugForms drugForms;
    private SlurEffectState slurEffectState;
    private DrugHandler drugHandler;
    private AddictiveExperienceConfiguration addictiveExperienceConfiguration;

    @Override
    public @NotNull DrugRegistry drugRegistry() {
        return this.drugRegistry;
    }

    @Override
    public @NotNull IPlantHandler plantHandler() {
        return this.plantHandler;
    }

    @Override
    public @NotNull IDrugForms drugForms() {
        return this.drugForms;
    }

    @Override
    public @NotNull SlurEffectState slurEffectState() {
        return this.slurEffectState;
    }

    @Override
    public @NotNull DrugHandler drugHandler() {
        return this.drugHandler;
    }

    public @NotNull AddictiveExperienceConfiguration addictiveExperienceConfiguration() {
        return this.addictiveExperienceConfiguration;
    }

    private void initializeInjector() {
        final Injector injector = Guice
                .createInjector(Stage.PRODUCTION, new AddictiveExperienceModule(this));
        this.drugForms = injector.getInstance(IDrugForms.class);
        this.slurEffectState = injector.getInstance(SlurEffectState.class);
        this.drugHandler = injector.getInstance(DrugHandler.class);
        // Missing bindings/implementations
        this.addictiveExperienceConfiguration = injector.getInstance(
                AddictiveExperienceConfiguration.class);
        this.drugRegistry = injector.getInstance(DrugRegistry.class);
        this.plantHandler = injector.getInstance(IPlantHandler.class);
        this.injector = injector;
    }

    private void shutdownTasks() {
        final PlantHandlerImpl plantHandlerImpl = (PlantHandlerImpl) this.plantHandler;
        plantHandlerImpl.shutdown();
    }

    private void shutdownInjector() {
    }

    @Override
    public void onDisable() {
        super.onDisable();
        shutdownTasks();
        shutdownInjector();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        initializeInjector();
    }
}
