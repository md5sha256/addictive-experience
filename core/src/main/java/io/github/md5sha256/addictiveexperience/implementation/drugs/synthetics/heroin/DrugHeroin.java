package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.ISynthetic;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components.Morphine;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components.Opium;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components.PlantOpium;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components.SeedOpium;
import io.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
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
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class DrugHeroin extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;
    private final DrugMeta defaultMeta;

    @Inject
    DrugHeroin(@NotNull Plugin plugin,
               @NotNull ItemFactory itemFactory,
               @NotNull DrugRegistry drugRegistry,
               @NotNull Morphine morphine,
               @NotNull Opium opium,
               @NotNull PlantOpium plantOpium,
               @NotNull SeedOpium seedOpium,
               @NotNull DrugForms forms
               ) {
        super(itemFactory,
              Utils.internalKey("heroin"),
              "Heroin",
              Material.GRAY_DYE,
              "addictiveexperience.consumeheroin");
        this.defaultMeta = DrugMeta.DEFAULT
                .toBuilder()
                .slurEffect(null)
                .overdoseThreshold(40)
                .potionEffects(
                        new PotionEffect(PotionEffectType.STRENGTH, 100, 2),
                        new PotionEffect(PotionEffectType.BLINDNESS, 60, 2)
                )
                .build();
        drugRegistry.registerComponent(this);
        this.recipe = createRecipe(plugin, forms.syringe(), morphine, drugRegistry);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin, @NotNull IDrugForm form, @NotNull Morphine morphine, DrugRegistry registry) {
        final NamespacedKey key = new NamespacedKey(plugin, "heroin");
        final ShapedRecipe recipe = new ShapedRecipe(key, registry.itemForComponent(this));
        recipe.shape("  $", " $ ", "$  ");
        recipe.setIngredient('$', new RecipeChoice.ExactChoice(registry.itemForComponent(morphine)));
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

    @Override
    public @NotNull DrugMeta defaultMeta() {
        return this.defaultMeta;
    }
}
