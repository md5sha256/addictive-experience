package com.github.md5sha256.addictiveexperience.api.drugs.impl;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DrugItemDataImpl implements DrugItemData {

    private final IDrugComponent component;
    private final IDrugForm drugForm;

    public DrugItemDataImpl(@NotNull IDrugComponent component, @NotNull IDrugForm form) {
        this.component = Objects.requireNonNull(component);
        this.drugForm = Objects.requireNonNull(form);
    }

    @NotNull
    @Override
    public IDrugComponent component() {
        return this.component;
    }

    @Override
    public @NotNull IDrugForm form() {
        return this.drugForm;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugItemDataImpl that = (DrugItemDataImpl) o;

        if (!component.equals(that.component)) return false;
        return drugForm.equals(that.drugForm);
    }

    @Override
    public int hashCode() {
        int result = component.hashCode();
        result = 31 * result + drugForm.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DrugItemData{" +
                "component=" + component +
                ", drugForm=" + drugForm +
                '}';
    }
}
