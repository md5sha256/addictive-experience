package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class PlantSafrole extends AbstractDrugComponent {

    private final Recipe recipe;
    private final SmeltingMeta smeltingMeta = SmeltingMeta.builder()
                                                          .smeltProductQuantity(1)
                                                          .experienceGain(100.0f)
                                                          .cookTimeTicks(500)
                                                          .build();

    @Inject
    PlantSafrole(@NotNull Plugin plugin,
                 @NotNull ItemFactory itemFactory,
                 @NotNull BarkSafrole barkSafrole,
                 @NotNull DrugRegistry registry) {
        super(itemFactory, Utils.internalKey("plant_safrole"), "Safrole", Material.SUNFLOWER);
        registry.registerComponent(this);
        this.recipe = createRecipe(plugin, barkSafrole, registry);
    }

    private Recipe createRecipe(@NotNull Plugin plugin, @NotNull BarkSafrole barkSafrole, DrugRegistry registry) {
        final NamespacedKey key = new NamespacedKey(plugin, "safrole");
        final RecipeChoice choiceBarkSafrole = new RecipeChoice.ExactChoice(barkSafrole.asItem(registry));
        return new FurnaceRecipe(key,
                                 asItem(registry).asQuantity(this.smeltingMeta.smeltProductQuantity()),
                                 choiceBarkSafrole,
                                 this.smeltingMeta.experienceGain(),
                                 this.smeltingMeta.cookTimeTicks());
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.SUNFLOWER);
        AdventureUtils.setDisplayName(meta, Component.text("Safrole", NamedTextColor.GOLD));
        final List<Component> lore = Arrays.asList(
                Component.text("A key ingredient to the", NamedTextColor.WHITE),
                Component.text("production of Ecstasy", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

}
