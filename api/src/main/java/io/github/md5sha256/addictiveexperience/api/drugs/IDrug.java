package io.github.md5sha256.addictiveexperience.api.drugs;

import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IDrug extends IDrugComponent {

    @NotNull String permission();

    @NotNull DrugMeta defaultMeta();

    @NotNull IDrugForm defaultForm();

    @Override
    default @NotNull ItemStack asItem(DrugRegistry registry) {
        return registry.itemForDrug(this, defaultForm());
    }

}
