package io.github.md5sha256.addictiveexperience.implementation.forms;

import com.google.inject.Inject;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import io.github.md5sha256.addictiveexperience.util.Utils;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FormGas extends AbstractDrugForm {

    private final DrugRegistry registry;

    @Inject
    FormGas(@NotNull DrugRegistry registry, @NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("gas"), "gas");
        this.registry = registry;
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<@NotNull ItemMeta> asMeta() {
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull IDrug drug) {
        return drug.asFunctionalItem(this.registry);
    }

    @Override
    public @NotNull ItemMeta asMeta(@NotNull IDrug drug) {
        return drug.asMeta();
    }


}
