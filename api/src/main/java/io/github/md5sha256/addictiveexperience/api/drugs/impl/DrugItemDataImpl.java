package io.github.md5sha256.addictiveexperience.api.drugs.impl;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class DrugItemDataImpl implements DrugItemData {

    private final IDrug drug;
    private final IDrugForm drugForm;

    public DrugItemDataImpl(@NotNull IDrug drug, @NotNull IDrugForm form) {
        this.drug = Objects.requireNonNull(drug);
        this.drugForm = Objects.requireNonNull(form);
    }

    @NotNull
    @Override
    public IDrug drug() {
        return this.drug;
    }

    @Override
    public @NotNull IDrugForm form() {
        return this.drugForm;
    }

    @Override
    public int hashCode() {
        int result = drug.hashCode();
        result = 31 * result + drugForm.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugItemDataImpl that = (DrugItemDataImpl) o;

        if (!Objects.equals(drug, that.drug)) return false;
        return drugForm.equals(that.drugForm);
    }

    @Override
    public String toString() {
        return "DrugItemData{" +
                "drug=" + drug +
                ", drugForm=" + drugForm +
                '}';
    }
}
