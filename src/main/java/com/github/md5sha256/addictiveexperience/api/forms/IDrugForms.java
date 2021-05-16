package com.github.md5sha256.addictiveexperience.api.forms;

import com.github.md5sha256.addictiveexperience.implementation.forms.blunt.IBlunts;
import org.jetbrains.annotations.NotNull;

public interface IDrugForms {

    @NotNull IDrugForm defaultForm();

    @NotNull IDrugForm powder();

    @NotNull IDrugForm syringe();

    @NotNull IBlunts blunt();

}
