package io.github.md5sha256.addictiveexperience.implementation.drugs;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface DrugItemDataFactory {

    @NotNull Optional<DrugItemData> parseData(@NotNull ItemStack itemStack);

    @NotNull Optional<IDrugComponent> parseComponent(@NotNull ItemStack itemStack);

    void data(@NotNull ItemStack itemStack, @NotNull DrugItemData itemData);

    void data(@NotNull ItemStack itemStack, @NotNull IDrugComponent component);

}
