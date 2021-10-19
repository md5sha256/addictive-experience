package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class SeedSafrole extends AbstractDrugComponent {

    @Inject
    SeedSafrole(@NotNull ItemFactory itemFactory) {
        super(itemFactory,
              Utils.internalKey("sapling_safrole"),
              "Safrole Sapling",
              Material.OAK_SAPLING);
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.OAK_SAPLING);
        AdventureUtils
                .setDisplayName(meta, Component.text("Safrole Sapling", NamedTextColor.GOLD));
        final List<Component> lore = Arrays.asList(
                Component.text("Used to harvest Safrole!", NamedTextColor.WHITE),
                Component.text("- the ingredient used to ", NamedTextColor.WHITE),
                Component.text("create Ecstasy!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.empty();
    }
}
