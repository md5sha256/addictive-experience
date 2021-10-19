package com.github.md5sha256.addictiveexperience.implementation.drugs.organics.marijuana;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IOrganic;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import com.github.md5sha256.addictiveexperience.implementation.drugs.organics.marijuana.components.PlantMarijuana;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public final class DrugMarijuana extends AbstractDrug implements IOrganic {

    private final DrugMeta defaultMeta;

    @Inject
    DrugMarijuana(@NotNull ItemFactory itemFactory,
                  @NotNull DrugRegistry drugRegistry,
                  @NotNull PlantMarijuana plantMarijuana
    ) {
        super(
                itemFactory,
                Utils.internalKey("marijuana"),
                "Marijuana",
                Material.GREEN_DYE,
                "addictiveexperience.consumeweed"
        );
        this.defaultMeta =
                DrugMeta.DEFAULT
                        .toBuilder()
                        .enabled(true)
                        .potionEffects(
                                new PotionEffect(PotionEffectType.INVISIBILITY, 100, 2),
                                new PotionEffect(PotionEffectType.LEVITATION, 300, 2)
                        )
                        .slurDurationMillis(TimeUnit.MINUTES.toMillis(5))
                        .overdoseThreshold(3000)
                        .build();
        drugRegistry.registerComponent(this, plantMarijuana);
        drugRegistry.metaData(plantMarijuana, DrugPlantMeta.KEY, DrugPlantMeta.defaultMeta(this));
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.GREEN_DYE);
        AdventureUtils.setDisplayName(meta, Component.text("Marijuana", NamedTextColor.DARK_GREEN));
        final List<Component> lore = Arrays.asList(
                Component.text("Marijuana is a psychoactive drug", NamedTextColor.WHITE),
                Component.text("from the Cannabis plant used for", NamedTextColor.WHITE),
                Component.text("medical and recreational purposes.", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }

    @Override
    public @NotNull DrugMeta defaultMeta() {
        return this.defaultMeta;
    }
}
