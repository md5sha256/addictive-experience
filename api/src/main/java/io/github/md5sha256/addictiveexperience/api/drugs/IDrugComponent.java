package io.github.md5sha256.addictiveexperience.api.drugs;

import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IDrugComponent extends Keyed {

    @NotNull String identifierName();

    @NotNull String displayName();

    @NotNull ItemStack itemModel();

    default @NotNull ItemStack asItem(DrugRegistry registry) {
        return registry.itemForComponent(this);
    }

    @NotNull ItemMeta asMeta();

    @NotNull Optional<@NotNull Recipe> recipe();

    @NotNull Collection<@NotNull PassiveEffect> passiveEffects();

}
