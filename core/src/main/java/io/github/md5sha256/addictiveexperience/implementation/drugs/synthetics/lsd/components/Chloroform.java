package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.components;

import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import io.github.md5sha256.addictiveexperience.implementation.drugs.effects.EffectStunned;
import io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components.MethylChloride;
import io.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
import io.github.md5sha256.addictiveexperience.util.Utils;
import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Chloroform extends AbstractDrug {

    private final Recipe recipe;
    private final DrugMeta defaultMeta;

    @Inject
    Chloroform(
            @NotNull Plugin plugin,
            @NotNull ItemFactory itemFactory,
            @NotNull MethylChloride mcl,
            @NotNull DrugRegistry registry,
            @NotNull DrugForms drugForms,
            @NotNull EffectStunned stunEffect
            ) {
        super(
                itemFactory,
                Utils.internalKey("chloroform"),
                "Chloroform",
                Material.PAPER,
                "addictiveexperience.consumechloroform",
                drugForms.gas()
        );
        registry.registerComponent(this);
        this.recipe = createRecipe(plugin, mcl, registry);
        int tenSecondsInTicks = Tick.tick().fromDuration(Duration.ofSeconds(10));
        this.defaultMeta =
                DrugMeta.DEFAULT.toBuilder()
                        .enabled(true)
                        .potionEffects(
                                new PotionEffect(PotionEffectType.BLINDNESS,
                                        tenSecondsInTicks,
                                        1000000),
                                new PotionEffect(PotionEffectType.NAUSEA,
                                        tenSecondsInTicks,
                                        1000000)
                        )
                        .customEffects(stunEffect)
                        .overdoseThreshold(3000)
                        .build();
    }

    private Recipe createRecipe(@NotNull Plugin plugin,
                                @NotNull MethylChloride mcl,
                                @NotNull DrugRegistry registry) {
        final NamespacedKey key = new NamespacedKey(plugin, "chloroform");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, asItem(registry).asQuantity(3));
        recipe.addIngredient(Material.PAPER);
        recipe.addIngredient(Material.WATER_BUCKET);
        recipe.addIngredient(mcl.asItem(registry));
        return recipe;
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.PAPER);
        final Component displayName = Component.text("Chloroform", NamedTextColor.AQUA);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Key ingredient in the production of LSD!", NamedTextColor.WHITE),
                Component.text("can also be used to knockout other Players!", NamedTextColor.WHITE)
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
