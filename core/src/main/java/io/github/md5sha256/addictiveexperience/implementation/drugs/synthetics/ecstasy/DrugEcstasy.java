package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.BarkSafrole;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.Mercury;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.MethylChloride;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.PlantSafrole;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.SeedSafrole;
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
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class DrugEcstasy extends AbstractDrug {

    private final Recipe recipe;
    private final DrugMeta defaultMeta;

    @Inject
    DrugEcstasy(@NotNull Plugin plugin,
                @NotNull ItemFactory itemFactory,
                @NotNull DrugRegistry drugRegistry,
                @NotNull DrugForms forms,
                @NotNull BarkSafrole barkSafrole,
                @NotNull PlantSafrole safrole,
                @NotNull SeedSafrole seedSafrole,
                @NotNull MethylChloride mcl,
                @NotNull Mercury mercury
    ) {
        super(itemFactory,
              Utils.internalKey("ecstasy"),
              "Ecstasy", Material.IRON_NUGGET, "addictiveexperience.consumeecstasy");
        this.defaultMeta = DrugMeta.DEFAULT
                .toBuilder()
                .slurEffect(null)
                .overdoseThreshold(70)
                .potionEffects(
                        new PotionEffect(PotionEffectType.SPEED, 500, 2),
                        new PotionEffect(PotionEffectType.JUMP_BOOST, 1000, 5)
                )
                .build();
        drugRegistry.registerComponent(this);
        drugRegistry.metaData(safrole, DrugPlantMeta.KEY, DrugPlantMeta.defaultMeta(this, seedSafrole));
        this.recipe = createRecipe(plugin, safrole, mcl, mercury, forms.powder(), drugRegistry);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin,
                                         @NotNull PlantSafrole safrole,
                                         @NotNull MethylChloride mcl,
                                         @NotNull Mercury mercury,
                                         @NotNull IDrugForm form,
                                         @NotNull DrugRegistry registry
    ) {
        final NamespacedKey key = new NamespacedKey(plugin, "ecstasy");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, registry.itemForDrug(this, form));
        recipe.addIngredient(new RecipeChoice.ExactChoice(registry.itemForComponent(safrole)));
        recipe.addIngredient(new RecipeChoice.ExactChoice(registry.itemForComponent(mcl)));
        recipe.addIngredient(new RecipeChoice.ExactChoice(registry.itemForComponent(mercury)));
        recipe.addIngredient(Material.IRON_INGOT);
        return recipe;
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.CYAN_DYE);
        AdventureUtils.setDisplayName(meta, Component.text("Methamphetamine", NamedTextColor.BLUE));
        final List<Component> lore = Arrays.asList(
                Component.text("Methamphetamine is a strong", NamedTextColor.WHITE),
                Component.text("and highly addictive drug that", NamedTextColor.WHITE),
                Component.text("affects the central nervous system", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

    @Override
    public @NotNull DrugMeta defaultMeta() {
        return this.defaultMeta;
    }
}
