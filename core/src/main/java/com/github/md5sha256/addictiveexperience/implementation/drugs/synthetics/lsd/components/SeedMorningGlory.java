package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.lsd.components;

import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
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
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class SeedMorningGlory extends AbstractDrugComponent {

    @Inject
    SeedMorningGlory(@NotNull ItemFactory itemFactory) {
        super(itemFactory,
              Utils.internalKey("seed_morning-glory"),
              "Morning Glory Plant Seeds",
              Material.MELON_SEEDS);
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.MELON_SEEDS);
        final Component displayName = Component
                .text("Morning Glory Plant Seeds", NamedTextColor.AQUA);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Collections.singletonList(
                Component.text("Key ingredient in the production of LSD!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }
}
