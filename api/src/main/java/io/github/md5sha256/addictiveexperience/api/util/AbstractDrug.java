package io.github.md5sha256.addictiveexperience.api.util;

import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractDrug extends AbstractDrugComponent implements IDrug {

    private final String permission;
    private final IDrugForm defaultForm;

    protected AbstractDrug(@NotNull ItemFactory itemFactory,
                           @NotNull Key key,
                           @NotNull String name,
                           @NotNull Material material,
                           @NotNull String permission,
                           @NotNull IDrugForm defaultForm) {
        super(itemFactory, key, name, material);
        this.permission = Objects.requireNonNull(permission);
        this.defaultForm = defaultForm;
    }

    protected abstract @NotNull ItemMeta meta();

    @Override
    public @NotNull IDrugForm defaultForm() {
        return this.defaultForm;
    }

    @Override
    public @NotNull String permission() {
        return this.permission;
    }
}
