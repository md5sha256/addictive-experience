package com.github.md5sha256.addictiveexperience.api.util;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDrugForm implements IDrugForm {

    protected final ItemFactory itemFactory;
    private final Map<IDrug, ItemMeta> metaMap = new HashMap<>();
    private final Key key;
    private final String displayName;

    protected AbstractDrugForm(@NotNull ItemFactory itemFactory,
                               @NotNull Key key,
                               @NotNull String displayName) {
        this.key = key;
        this.displayName = displayName;
        this.itemFactory = itemFactory;
    }

    @Override
    public @NotNull String displayName() {
        return this.displayName;
    }

    @Override
    public boolean isDrug(@NotNull ItemStack itemStack, @NotNull IDrug drug) {
        final ItemMeta meta;
        if (!itemStack.hasItemMeta() || (meta = itemStack.getItemMeta()) == null) {
            return asItem(drug).isSimilar(itemStack);
        }
        return this.itemFactory.equals(meta, asMeta(drug));
    }

    @Override
    public boolean isDrug(@NotNull ItemMeta meta, @NotNull IDrug drug) {
        final ItemMeta drugMeta = this.metaMap.computeIfAbsent(drug, this::asMeta);
        return this.itemFactory.equals(drugMeta, meta);
    }

    @Override
    public final @NotNull Key key() {
        return this.key;
    }
}
