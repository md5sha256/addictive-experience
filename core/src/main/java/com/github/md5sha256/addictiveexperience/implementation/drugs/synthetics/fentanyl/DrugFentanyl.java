package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.fentanyl;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.ISynthetic;
import com.github.md5sha256.addictiveexperience.api.drugs.PassiveEffect;
import com.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import com.github.md5sha256.addictiveexperience.implementation.drugs.effects.EffectRandomDeath;
import com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.heroin.components.Opium;
import com.github.md5sha256.addictiveexperience.util.AdventureUtils;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.Common;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public final class DrugFentanyl extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;
    private final DrugMeta defaultMeta;
    private final Collection<PassiveEffect> effects;

    @Inject
    DrugFentanyl(@NotNull Plugin plugin,
                 @NotNull ItemFactory itemFactory,
                 @NotNull Opium opium,
                 @NotNull EffectRandomDeath randomDeath,
                 @NotNull DrugRegistry drugRegistry
    ) {
        super(itemFactory,
              Utils.internalKey("fentanyl"),
              "Fentanyl",
              Material.WHITE_DYE,
              "addictiveexperience.consumefentanyl");
        this.recipe = createRecipe(plugin, opium);
        final CustomEffect effectRandomDeath = randomDeath.createEffect(
                Common.toTicks(2, TimeUnit.MINUTES),
                TimeUnit.SECONDS.toMillis(30),
                0.5D
        );
        this.defaultMeta = DrugMeta.DEFAULT
                .toBuilder()
                .overdoseThreshold(50)
                .slurEffect(null)
                .potionEffects(
                        new PotionEffect(PotionEffectType.CONFUSION, 200, 3)
                )
                .customEffects(effectRandomDeath)
                .build();
        this.effects = Collections.singleton(randomDeath);
        drugRegistry.registerComponent(this);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin, @NotNull Opium opium) {
        final NamespacedKey key = new NamespacedKey(plugin, "fentanyl");
        final ShapedRecipe recipe = new ShapedRecipe(key, asItem(1));
        recipe.shape("$ $", " $ ", "$ $");
        recipe.setIngredient('$', opium.asItem(1));
        return recipe;
    }


    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

    protected @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.SUGAR);
        final Component displayName = Component.text("Fentanyl", NamedTextColor.WHITE);
        final List<Component> lore = Arrays.asList(
                Component.text("Fentanyl, also known as fentanil is a", NamedTextColor.WHITE),
                Component.text("a powerful anesthetic which is also", NamedTextColor.WHITE),
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

    @Override
    public @NotNull Collection<@NotNull PassiveEffect> passiveEffects() {
        return this.effects;
    }
}
