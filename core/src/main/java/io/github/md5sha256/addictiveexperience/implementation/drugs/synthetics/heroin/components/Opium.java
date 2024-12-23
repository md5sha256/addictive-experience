package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.SmeltingMeta;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Opium extends AbstractDrugComponent {

    private final Recipe recipe;

    private final SmeltingMeta smeltingMeta =
            SmeltingMeta.builder()
                        .experienceGain(75.0f)
                        .cookTimeTicks(800)
                        .smeltProductQuantity(1)
                        .build();

    @Inject
    Opium(@NotNull Plugin plugin,
          @NotNull ItemFactory itemFactory,
          @NotNull SeedOpium seedOpium,
          @NotNull PlantOpium plantOpium,
          @NotNull DrugRegistry drugRegistry
          ) {
        super(itemFactory, Utils.internalKey("opium"), "Opium", Material.LIGHT_GRAY_DYE);
        drugRegistry.registerComponent(this);
        drugRegistry.metaData(plantOpium, DrugPlantMeta.KEY, DrugPlantMeta.defaultMeta(this, seedOpium));
        this.recipe = createRecipe(plugin, seedOpium, drugRegistry);
    }

    private Recipe createRecipe(@NotNull Plugin plugin, @NotNull SeedOpium seedOpium, DrugRegistry registry) {
        final NamespacedKey key = new NamespacedKey(plugin, "opium");
        final RecipeChoice choiceOpium = new RecipeChoice.ExactChoice(registry.itemForComponent(seedOpium));
        return new FurnaceRecipe(key,
                                 registry.itemForComponent(this).asQuantity(this.smeltingMeta.smeltProductQuantity()),
                                 choiceOpium,
                                 this.smeltingMeta.experienceGain(),
                                 this.smeltingMeta.cookTimeTicks());
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.LIGHT_GRAY_DYE);
        final Component displayName = Component.text("Opium", NamedTextColor.RED);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Collections.singletonList(
                Component.text("Used to create Morphine!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }
}
