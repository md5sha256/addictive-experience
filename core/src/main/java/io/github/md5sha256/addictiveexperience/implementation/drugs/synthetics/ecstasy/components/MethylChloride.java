package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components;

import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
public final class MethylChloride extends AbstractDrugComponent {

    private final Recipe recipe;

    @Inject
    MethylChloride(@NotNull Plugin plugin, @NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("methylchloride"), "MethylChloride", Material.GLASS_BOTTLE);
        this.recipe = createRecipe(plugin);
    }

    private Recipe createRecipe(@NotNull Plugin plugin) {
        final NamespacedKey key = new NamespacedKey(plugin, "methylchloride");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, this.asItem());
        recipe.addIngredient(Material.GLASS_BOTTLE);
        recipe.addIngredient(Material.BLAZE_POWDER);
        recipe.addIngredient(Material.NETHER_WART);
        recipe.addIngredient(Material.GUNPOWDER);
        return recipe;
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.GLASS_BOTTLE);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        final Component displayName = Component.text("Methylamine Chloride", NamedTextColor.GOLD);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("This gas is a key ingredient", NamedTextColor.WHITE),
                Component.text("to the production of Ecstasy", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.of(this.recipe);
    }
}
