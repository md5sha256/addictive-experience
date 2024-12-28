package io.github.md5sha256.addictiveexperience.fixtures;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DummyDrugImpl extends AbstractDrug {

    private final ItemMeta meta;

    public DummyDrugImpl(@NotNull ItemFactory factory, @NotNull Key key,
                         @NotNull String name,
                         @NotNull Material material,
                         @NotNull String permission,
                         @NotNull IDrugForm form) {
        super(factory, key, name, material, permission, form);
        this.meta = factory.getItemMeta(material);
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }

    @Override
    protected @NotNull ItemMeta meta() {
        return this.meta;
    }

    @Override
    public @NotNull DrugMeta defaultMeta() {
        return DrugMeta.DEFAULT;
    }
}
