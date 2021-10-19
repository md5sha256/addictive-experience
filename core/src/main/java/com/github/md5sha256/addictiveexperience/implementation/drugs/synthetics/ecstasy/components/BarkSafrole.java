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
public final class BarkSafrole extends AbstractDrugComponent {

    @Inject
    BarkSafrole(@NotNull ItemFactory itemFactory) {
        super(itemFactory,
              Utils.internalKey("bark_safrole"),
              "Safrole Bark",
              Material.NETHER_BRICK);
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.NETHER_BRICK);
        AdventureUtils.setDisplayName(meta, Component.text("Safrole Bark", NamedTextColor.GOLD));
        final List<Component> lore = Arrays.asList(
                Component.text("Used to obtain Safrole!", NamedTextColor.WHITE),
                Component.text("Which is a key ingredient", NamedTextColor.WHITE),
                Component.text("in the production of Ecstasy", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }

}
