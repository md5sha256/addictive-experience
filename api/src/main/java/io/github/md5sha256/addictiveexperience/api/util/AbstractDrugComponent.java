package io.github.md5sha256.addictiveexperience.api.util;

import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.drugs.PassiveEffect;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public abstract class AbstractDrugComponent implements IDrugComponent {

    private final Key key;
    private final String name;
    private final ItemMeta meta;
    private final ItemStack item;
    protected final ItemFactory itemFactory;

    protected AbstractDrugComponent(@NotNull ItemFactory itemFactory,
                                    @NotNull String name,
                                    @NotNull Key key,
                                    @NotNull Material material) {
        this(itemFactory, key, name, material);
    }

    protected AbstractDrugComponent(@NotNull ItemFactory itemFactory,
                                    @NotNull Key key,
                                    @NotNull String name,
                                    @NotNull Material material) {
        this.itemFactory = Objects.requireNonNull(itemFactory);
        this.name = Objects.requireNonNull(name);
        this.key = key;
        this.item = ItemStack.of(material);
        this.meta = meta();
        this.item.setItemMeta(this.meta);
    }

    @Override
    public @NotNull Key key() {
        return this.key;
    }

    @Override
    public @NotNull String identifierName() {
        return this.name;
    }

    @Override
    public @NotNull String displayName() {
        return this.name;
    }

    @Override
    public @NotNull ItemStack asItem() {
        return this.item.clone();
    }

    @Override
    public @NotNull ItemMeta asMeta() {
        return this.meta.clone();
    }

    protected abstract @NotNull ItemMeta meta();

    @Override
    public @NotNull Collection<@NotNull PassiveEffect> passiveEffects() {
        return Collections.emptySet();
    }
}
