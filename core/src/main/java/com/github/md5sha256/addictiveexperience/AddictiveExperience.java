package com.github.md5sha256.addictiveexperience;

import com.github.md5sha256.addictiveexperience.api.AddictiveExperienceApi;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.github.md5sha256.addictiveexperience.configuration.DrugFunConfiguration;
import com.github.md5sha256.addictiveexperience.module.AddictiveExperienceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AddictiveExperience extends JavaPlugin implements AddictiveExperienceApi {

    private Injector injector;
    private DrugRegistry drugRegistry;
    private IDrugForms drugForms;
    private SlurEffectState slurEffectState;
    private DrugHandler drugHandler;
    private DrugFunConfiguration drugFunConfiguration;

    @Override
    public @NotNull DrugRegistry drugRegistry() {
        return this.drugRegistry;
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

    public @NotNull DrugFunConfiguration drugFunConfiguration() {
        return this.drugFunConfiguration;
    }

    private void initializeInjector() {
        final Injector injector = Guice
                .createInjector(Stage.PRODUCTION, new AddictiveExperienceModule(this));
        this.drugForms = injector.getInstance(IDrugForms.class);
        this.slurEffectState = injector.getInstance(SlurEffectState.class);
        this.drugHandler = injector.getInstance(DrugHandler.class);
        // Missing bindings/implementations
        this.drugFunConfiguration = injector.getInstance(DrugFunConfiguration.class);
        this.drugRegistry = injector.getInstance(DrugRegistry.class);
        this.injector = injector;
    }

    private void shutdownInjector() {
        this.injector = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        shutdownInjector();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        initializeInjector();
    }
}
