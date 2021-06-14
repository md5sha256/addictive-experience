package com.github.md5sha256.addictiveexperience.implementation.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface DrugItemDataFactory {

    @NotNull Optional<DrugItemData> dataFor(@NotNull ItemStack itemStack);

    void data(@NotNull ItemStack itemStack, @NotNull DrugItemData itemData);
}
