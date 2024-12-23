package io.github.md5sha256.addictiveexperience.module;

import io.github.md5sha256.addictiveexperience.api.forms.IBlunts;
import io.github.md5sha256.addictiveexperience.implementation.drugs.organics.marijuana.DrugMarijuana;
import io.github.md5sha256.addictiveexperience.implementation.drugs.organics.psiolcybin.MushroomPsilocybin;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.cocaine.DrugCocaine;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.DrugEcstasy;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.DrugHeroin;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.DrugLSD;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.meth.DrugMethamphetamine;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.meth.components.PlantEphedrine;
import io.github.md5sha256.addictiveexperience.implementation.forms.blunt.Blunts;
import com.google.inject.AbstractModule;

public final class DrugsModule extends AbstractModule {

    @Override
    protected void configure() {
        // TODO refactor to individual modules
        bind(IBlunts.class).to(Blunts.class).asEagerSingleton();
        // Organics
        bind(DrugMarijuana.class).asEagerSingleton();
        bind(MushroomPsilocybin.class).asEagerSingleton();

        // Synthetics
        bind(DrugCocaine.class).asEagerSingleton();
        bind(DrugEcstasy.class).asEagerSingleton();
        bind(DrugHeroin.class).asEagerSingleton();
        bind(DrugLSD.class).asEagerSingleton();
        bind(DrugMethamphetamine.class).asEagerSingleton();
        bind(PlantEphedrine.class).asEagerSingleton();
    }
}
