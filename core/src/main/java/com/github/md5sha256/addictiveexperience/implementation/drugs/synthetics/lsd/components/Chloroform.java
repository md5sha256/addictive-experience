package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.components;

import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.MethylChloride;
import com.github.md5sha256.addictiveexperience.util.AdventureUtils;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Chloroform extends AbstractDrugComponent {

    private final Recipe recipe;

    @Inject
    Chloroform(@NotNull Plugin plugin, @NotNull ItemFactory itemFactory, @NotNull MethylChloride mcl) {
        super(itemFactory, Utils.internalKey("chloroform"), "Chloroform", Material.PAPER);
        this.recipe = createRecipe(plugin, mcl);
    }

    private Recipe createRecipe(@NotNull Plugin plugin, @NotNull MethylChloride mcl) {
        final NamespacedKey key = new NamespacedKey(plugin, "chloroform");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, this.asItem(3));
        recipe.addIngredient(Material.PAPER);
        recipe.addIngredient(Material.WATER_BUCKET);
        recipe.addIngredient(mcl.asItem());
        return recipe;
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.PAPER);
        final Component displayName = Component.text("Chloroform", NamedTextColor.AQUA);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Key ingredient in the production of LSD!", NamedTextColor.WHITE),
                Component.text("can also be used to knockout other Players!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }
}
