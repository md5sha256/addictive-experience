package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.cocaine.components;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import com.github.md5sha256.addictiveexperience.util.AdventureUtils;
import com.github.md5sha256.addictiveexperience.util.Utils;
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
public final class PlantCocaine extends AbstractDrugComponent implements IDrugComponent {

    @Inject
    PlantCocaine(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("plant_cocaine"), "Coca Plant", Material.TALL_GRASS);
    }

    protected final @NotNull ItemMeta meta() {
        ItemMeta meta = this.itemFactory.getItemMeta(Material.KELP);
        AdventureUtils.setDisplayName(meta, Component.text("Coca Plant", NamedTextColor.WHITE));
        final List<Component> lore = Collections.singletonList(
                Component.text("Used to farm Coca Leaves!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.empty();
    }
}
