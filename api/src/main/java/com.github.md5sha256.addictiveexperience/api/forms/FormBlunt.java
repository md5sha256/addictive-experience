package com.github.md5sha256.addictiveexperience.api.forms;

import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;

public interface FormBlunt extends IDrugForm {

    @NotNull BluntState bluntState();

}
