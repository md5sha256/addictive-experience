package com.github.md5sha256.addictiveexperience.api.forms;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IDrugForm extends Keyed {

    @NotNull String displayName();

    @NotNull Optional<@NotNull ItemStack> asItem();

    @NotNull ItemStack asItem(@NotNull IDrug drug);

    @NotNull ItemMeta asMeta(@NotNull IDrug drug);

    @NotNull Optional<@NotNull ItemMeta> asMeta();

    boolean isDrug(@NotNull ItemStack itemStack, @NotNull IDrug drug);

    boolean isDrug(@NotNull ItemMeta meta, @NotNull IDrug drug);

}
