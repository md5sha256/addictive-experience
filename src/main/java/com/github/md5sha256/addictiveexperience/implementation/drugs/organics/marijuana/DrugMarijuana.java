package com.github.md5sha256.addictiveexperience.implementation.drugs.organics.marijuana;

import com.github.md5sha256.addictiveexperience.api.drugs.IOrganic;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import com.github.md5sha256.addictiveexperience.util.AdventureUtils;
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
public final class DrugMarijuana extends AbstractDrug implements IOrganic {

    @Inject
    DrugMarijuana(@NotNull ItemFactory itemFactory
    ) {
        super(itemFactory, "Marijuana", Material.GREEN_DYE, "addictiveexperience.consumeweed");
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

}
