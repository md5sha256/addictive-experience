package com.github.md5sha256.addictiveexperience.module;

import com.github.md5sha256.addictiveexperience.api.AddictiveExperience;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.github.md5sha256.addictiveexperience.implementation.DrugHandlerImpl;
import com.github.md5sha256.addictiveexperience.implementation.SlurEffectStateImpl;
import com.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

public final class AddictiveExperienceModule extends AbstractModule {

    private final AddictiveExperience api;

    public AddictiveExperienceModule(@NotNull AddictiveExperience api) {
        this.api = api;
    }

    @Override
    protected void configure() {
        // Drugs Implementation
        install(new DrugsModule());

        // Implementation
        bind(DrugHandler.class).to(DrugHandlerImpl.class).asEagerSingleton();
        bind(SlurEffectState.class).to(SlurEffectStateImpl.class).asEagerSingleton();

        // API bindings
        bind(IDrugForms.class).to(DrugForms.class).in(Singleton.class);
        bind(AddictiveExperience.class).toInstance(this.api);
    }
}
