package com.github.md5sha256.addictiveexperience.implementation.forms;

import com.github.md5sha256.addictiveexperience.api.forms.IBlunts;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class DrugForms implements IDrugForms {

    private final IDrugForm formPowder;
    private final IDrugForm formSyringe;
    private final IDrugForm formDefault;
    private final IBlunts blunts;

    @Inject
    DrugForms(
            @NotNull FormDefault formDefault,
            @NotNull FormPowder formPowder,
              @NotNull FormSyringe formSyringe,
              @NotNull IBlunts blunts
    ) {
        this.formDefault = formDefault;
        this.formPowder = formPowder;
        this.formSyringe = formSyringe;
        this.blunts = blunts;
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
    public @NotNull IBlunts blunt() {
        return this.blunts;
    }

}
