package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.components;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Ethanol extends AbstractDrugComponent {

    private final Recipe recipe;

    @Inject
    Ethanol(@NotNull Plugin plugin, @NotNull ItemFactory itemFactory, DrugRegistry registry) {
        super(itemFactory, Utils.internalKey("ethanol"), "Ethanol", Material.WATER_BUCKET);
        registry.registerComponent(this);
        this.recipe = createRecipe(plugin, registry);
    }

    private Recipe createRecipe(@NotNull Plugin plugin, @NotNull DrugRegistry registry) {
        final NamespacedKey key = new NamespacedKey(plugin, "ethanol");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, asItem(registry));
        recipe.addIngredient(Material.WATER_BUCKET);
        recipe.addIngredient(Material.SUGAR_CANE);
        recipe.addIngredient(Material.BEETROOT);
        return recipe;
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.WATER_BUCKET);
        final Component displayName = Component.text("Ethanol", NamedTextColor.AQUA);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Collections.singletonList(
                Component.text("Key ingredient in the production of LSD!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }
}
