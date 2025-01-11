package io.github.md5sha256.addictiveexperience.api.forms;

import org.jetbrains.annotations.NotNull;

public interface IDrugForms {

    @NotNull IDrugForm defaultForm();

    @NotNull IDrugForm powder();

    @NotNull IDrugForm syringe();

    @NotNull IDrugForm gas();

    @NotNull IBlunts blunt();

}
