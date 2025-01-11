package io.github.md5sha256.addictiveexperience.implementation.forms;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.forms.IBlunts;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class DrugForms implements IDrugForms {

    private final IDrugForm formPowder;
    private final IDrugForm formSyringe;
    private final IDrugForm formDefault;
    private final IDrugForm formGas;
    private final IBlunts blunts;

    @Inject
    DrugForms(
            @NotNull FormDefault formDefault,
            @NotNull FormPowder formPowder,
            @NotNull FormSyringe formSyringe,
            @NotNull FormGas formGas,
            @NotNull IBlunts blunts,
            @NotNull DrugRegistry drugRegistry
    ) {
        this.formDefault = formDefault;
        this.formPowder = formPowder;
        this.formSyringe = formSyringe;
        this.formGas = formGas;
        this.blunts = blunts;
        drugRegistry.registerDrugForm(
                formPowder,
                formSyringe,
                formDefault,
                blunts.lit(),
                blunts.unlit()
        );
    }

    @Override
    public @NotNull IDrugForm defaultForm() {
        return this.formDefault;
    }

    @Override
    public @NotNull IDrugForm powder() {
        return this.formPowder;
    }

    @Override
    public @NotNull IDrugForm syringe() {
        return this.formSyringe;
    }

    @Override
    public @NotNull IDrugForm gas() {
        return this.formGas;
    }

    @Override
    public @NotNull IBlunts blunt() {
        return this.blunts;
    }

}
