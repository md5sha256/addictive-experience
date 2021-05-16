package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugItemDataImpl;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;

public interface DrugItemData {

    @NotNull
    static DrugItemData of(@NotNull IDrug drug, @NotNull IDrugForm drugForm) {
        return new DrugItemDataImpl(drug, drugForm);
    }

    @NotNull IDrug drug();

    @NotNull IDrugForm form();

}
