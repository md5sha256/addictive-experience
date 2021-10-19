package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components;

import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Morphine extends AbstractDrugComponent {

    private final Recipe recipe;

    @Inject
    Morphine(@NotNull Plugin plugin, @NotNull ItemFactory itemFactory, @NotNull Opium opium) {
        super(itemFactory, Utils.internalKey("morphine"), "Morphine", Material.LIGHT_GRAY_DYE);
        this.recipe = createRecipe(plugin, opium);
    }

    private Recipe createRecipe(@NotNull Plugin plugin, @NotNull Opium opium) {
        final NamespacedKey key = new NamespacedKey(plugin, "morphine");
        final ShapedRecipe recipe = new ShapedRecipe(key, this.asItem());
        recipe.shape("$$$", "$$$", "$$$");
        recipe.setIngredient('$', new RecipeChoice.ExactChoice(opium.asItem()));
        return recipe;
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.LIGHT_GRAY_DYE);
        final Component displayName = Component.text("Morphine", NamedTextColor.RED);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Collections.singletonList(
                Component.text("Used to create Heroin!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }
}
