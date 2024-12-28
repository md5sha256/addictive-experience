package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.cocaine;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.ISynthetic;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.cocaine.components.LeafCocaine;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.cocaine.components.PlantCocaine;
import io.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
import io.github.md5sha256.addictiveexperience.implementation.forms.FormPowder;
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
public final class DrugCocaine extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;
    private final DrugMeta defaultMeta;

    @Inject
    DrugCocaine(@NotNull Plugin plugin,
                @NotNull ItemFactory itemFactory,
                @NotNull PlantCocaine plantCocaine,
                @NotNull LeafCocaine leafCocaine,
                @NotNull DrugForms forms,
                @NotNull DrugRegistry drugRegistry
    ) {
        super(itemFactory,
              Utils.internalKey("cocaine"),
              "Cocaine",
              Material.SUGAR,
              "addictiveexperience.consumecocaine",
                forms.powder());
        this.defaultMeta = DrugMeta.DEFAULT
                .toBuilder()
                .overdoseThreshold(50)
                .slurEffect(null)
                .potionEffects(
                        new PotionEffect(PotionEffectType.SPEED, 400, 2),
                        new PotionEffect(PotionEffectType.JUMP_BOOST, 300, 2)
                )
                .build();
        drugRegistry.registerComponent(this);
        drugRegistry.metaData(plantCocaine, DrugPlantMeta.KEY, DrugPlantMeta.defaultMeta(this));
        this.recipe = createRecipe(plugin, leafCocaine, drugRegistry);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin, @NotNull LeafCocaine leafCocaine, DrugRegistry registry) {
        final NamespacedKey key = new NamespacedKey(plugin, "Cocaine");
        final ShapedRecipe recipe = new ShapedRecipe(key, asItem(registry));
        recipe.shape(" $ ", "$ $", " $ ");
        recipe.setIngredient('$', leafCocaine.asItem(registry));
        return recipe;
    }


    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.SUGAR);
        final Component displayName = Component.text("Cocaine", NamedTextColor.WHITE);
        final List<Component> lore = Arrays.asList(
                Component.text("Cocaine, also known as coke is a", NamedTextColor.WHITE),
                Component.text("strong stimulant most frequently", NamedTextColor.WHITE),
                Component.text("used as a recreational drug.", NamedTextColor.WHITE)
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
