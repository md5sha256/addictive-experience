package com.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.meth.components;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public final class PlantEphedrine extends AbstractDrugComponent implements IDrugComponent {

    @Inject
    PlantEphedrine(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("plant_ephedrine"), "Ephedrine Plant", Material.RED_TULIP);
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.empty();
    }

    @Override
    protected @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.RED_TULIP);
        AdventureUtils.setDisplayName(meta, Component.text("Ephedrine Plant", NamedTextColor.BLUE));
        final List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Use to harvest", NamedTextColor.WHITE));
        lore.add(Component.text("Ephedrine!", NamedTextColor.WHITE));
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

}
