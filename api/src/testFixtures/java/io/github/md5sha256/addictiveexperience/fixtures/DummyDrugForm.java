package io.github.md5sha256.addictiveexperience.fixtures;

import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DummyDrugForm extends AbstractDrugForm implements IDrugForm {


    public DummyDrugForm(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Key.key("dummy", "dummy"), "dummy");
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull IDrug drug) {
        return drug.itemModel();
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
