package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin;

import com.github.md5sha256.addictiveexperience.api.drugs.ISynthetic;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components.Morphine;
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
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class DrugHeroin extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;

    @Inject
    DrugHeroin(@NotNull Plugin plugin,
               @NotNull ItemFactory itemFactory,
               @NotNull Morphine morphine
    ) {
        super(itemFactory,
              Utils.internalKey("heroin"),
              "Heroin",
              Material.GRAY_DYE,
              "addictiveexperience.consumeheroin");
        this.recipe = createRecipe(plugin, morphine);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin, @NotNull Morphine morphine) {
        final NamespacedKey key = new NamespacedKey(plugin, "heroin");
        final ShapedRecipe recipe = new ShapedRecipe(key, this.asItem());
        recipe.shape("  $", " $ ", "$  ");
        recipe.setIngredient('$', new RecipeChoice.ExactChoice(morphine.asItem()));
        return recipe;
    }


    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.GRAY_DYE);
        final Component displayName = Component.text("Heroin", NamedTextColor.RED);
        final List<Component> lore = Arrays.asList(
                Component.text("Heroin, also known as Diamorphine among"),
                Component.text("other names, is an opioid most commonly"),
                Component.text("used as a recreational drug for "),
                Component.text("it's euphoric effects.")
        );
        AdventureUtils.setDisplayName(meta, displayName);
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

}
