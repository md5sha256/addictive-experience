package io.github.md5sha256.addictiveexperience.api;

import io.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import org.jetbrains.annotations.NotNull;

public interface AddictiveExperience {

    @NotNull DrugRegistry drugRegistry();

    @NotNull IPlantHandler plantHandler();

    @NotNull IDrugForms drugForms();

    @NotNull SlurEffectState slurEffectState();

    @NotNull DrugHandler drugHandler();

}
