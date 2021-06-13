package com.github.md5sha256.addictiveexperience.module;

import com.github.md5sha256.addictiveexperience.api.forms.IBlunts;
import com.github.md5sha256.addictiveexperience.implementation.drugs.organics.marijuana.DrugMarijuana;
import com.github.md5sha256.addictiveexperience.implementation.drugs.organics.psiolcybin.MushroomPsilocybin;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.cocaine.DrugCocaine;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.DrugEcstasy;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.DrugHeroin;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.DrugLSD;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.meth.DrugMethamphetamine;
import com.github.md5sha256.addictiveexperience.implementation.forms.blunt.Blunts;
import com.google.inject.AbstractModule;

public final class DrugsModule extends AbstractModule {

    @Override
    protected void configure() {
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
    }
}
