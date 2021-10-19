package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.components;

import com.github.md5sha256.addictiveexperience.api.drugs.SmeltingMeta;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class LysergicAcid extends AbstractDrugComponent {

    private final Recipe recipe;
    private final SmeltingMeta smeltingMeta = SmeltingMeta.builder()
                                                          .smeltProductQuantity(1)
                                                          .experienceGain(50f)
                                                          .cookTimeTicks(300).build();

    @Inject
    LysergicAcid(@NotNull Plugin plugin,
                 @NotNull ItemFactory itemFactory,
                 @NotNull SeedMorningGlory glorySeeds
    ) {
        super(itemFactory,
              Utils.internalKey("lysergic-acid"),
              "Lysergic Acid",
              Material.WATER_BUCKET);
        this.recipe = createRecipe(plugin, glorySeeds);
    }

    private Recipe createRecipe(@NotNull Plugin plugin, @NotNull SeedMorningGlory glorySeeds) {
        final NamespacedKey key = new NamespacedKey(plugin, "lysergic-acid");
        final RecipeChoice choiceGlorySeeds = new RecipeChoice.ExactChoice(glorySeeds.asItem());
        final ItemStack result = this.asItem(this.smeltingMeta.smeltProductQuantity());
        return new FurnaceRecipe(key,
                                 result,
                                 choiceGlorySeeds,
                                 this.smeltingMeta.experienceGain(),
                                 this.smeltingMeta.cookTimeTicks());
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.WATER_BUCKET);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        final Component displayName = Component.text("Lysergic Acid", NamedTextColor.AQUA);
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
