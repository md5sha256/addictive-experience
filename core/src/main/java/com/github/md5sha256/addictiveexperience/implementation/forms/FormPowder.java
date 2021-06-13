package com.github.md5sha256.addictiveexperience.implementation.forms;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Singleton
public final class FormPowder extends AbstractDrugForm implements IDrugForm {

    @Inject
    FormPowder(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("powder"), "Powder");
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull IDrug drug) {
        return drug.asItem();
    }

    @Override
    public @NotNull ItemMeta asMeta(@NotNull IDrug drug) {
        return drug.asMeta();
    }

    @Override
    public @NotNull Optional<@NotNull ItemMeta> asMeta() {
        return Optional.empty();
    }

}
