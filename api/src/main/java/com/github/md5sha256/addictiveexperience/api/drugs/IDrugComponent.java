package com.github.md5sha256.addictiveexperience.api.drugs;

import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IDrugComponent extends Keyed {

    @NotNull String identifierName();

    @NotNull String displayName();

    @NotNull ItemStack asItem();

    default @NotNull ItemStack asItem(int amount) {
        final ItemStack itemStack = asItem();
        itemStack.setAmount(amount);
        return itemStack;
    }

    @NotNull ItemMeta asMeta();

    @NotNull Optional<@NotNull Recipe> recipe();

}
