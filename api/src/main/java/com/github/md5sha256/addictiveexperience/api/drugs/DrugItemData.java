package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugItemDataImpl;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.util.SimilarLike;
import org.jetbrains.annotations.NotNull;

public interface DrugItemData extends SimilarLike<DrugItemData> {

    @NotNull
    static DrugItemData of(@NotNull IDrugComponent component, @NotNull IDrugForm drugForm) {
        return new DrugItemDataImpl(component, drugForm);
    }

    @NotNull IDrugComponent component();

    @NotNull IDrugForm form();

    @Override
    default boolean isSimilar(@NotNull DrugItemData other) {
        return this.component().equals(other.component()) && this.form().equals(other.form());
    }
}
