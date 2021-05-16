package com.github.md5sha256.addictiveexperience.api.util;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.util.Utils;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractDrugComponent implements IDrugComponent {

    private final Key key;
    private final String name;
    private final ItemMeta meta;
    private final ItemStack item;
    protected final ItemFactory itemFactory;

    protected AbstractDrugComponent(@NotNull ItemFactory itemFactory,
                                    @NotNull String name,
                                    @NotNull Material material) {
        this(itemFactory, Utils.internalKey(name), name, material);
    }

    protected AbstractDrugComponent(@NotNull ItemFactory itemFactory,
                                    @NotNull Key key,
                                    @NotNull String name,
                                    @NotNull Material material) {
        this.itemFactory = Objects.requireNonNull(itemFactory);
        this.name = Objects.requireNonNull(name);
        this.key = key;
        this.item = new ItemStack(material);
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

    @Override
    public boolean is(@NotNull ItemStack itemStack) {
        return itemStack.hasItemMeta()
                && this.itemFactory.equals(itemStack.getItemMeta(), this.meta);
    }

    @Override
    public boolean is(@NotNull ItemMeta meta) {
        return this.itemFactory.equals(this.meta, meta);
    }

    protected abstract @NotNull ItemMeta meta();
}
