package com.github.md5sha256.addictiveexperience.api.util;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractDrug extends AbstractDrugComponent implements IDrug {

    private final String permission;

    protected AbstractDrug(@NotNull ItemFactory itemFactory,
                           @NotNull Key key,
                           @NotNull String name,
                           @NotNull Material material,
                           @NotNull String permission) {
        super(itemFactory, key, name, material);
        this.permission = Objects.requireNonNull(permission);
    }

    protected abstract @NotNull ItemMeta meta();

    @Override
    public @NotNull String permission() {
        return this.permission;
    }
}
