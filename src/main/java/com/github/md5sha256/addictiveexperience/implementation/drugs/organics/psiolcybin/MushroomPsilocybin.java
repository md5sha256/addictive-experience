package com.github.md5sha256.addictiveexperience.implementation.drugs.organics.psiolcybin;

import com.github.md5sha256.addictiveexperience.api.drugs.IOrganic;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import com.github.md5sha256.addictiveexperience.util.AdventureUtils;
import com.github.md5sha256.addictiveexperience.util.Utils;
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

public class MushroomPsilocybin extends AbstractDrug implements IOrganic {

    public MushroomPsilocybin(@NotNull ItemFactory itemFactory) {
        super(itemFactory,
              Utils.internalKey("mushroom_psilocybin"),
              "Psilocybin Mushroom",
              Material.BROWN_MUSHROOM,
              // Original permission = drugfun.consumeshrooms
              "drugfun.consumepsilocybin");
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.BROWN_MUSHROOM);
        final Component displayName = Component
                .text("Psilocybin Mushroom", NamedTextColor.LIGHT_PURPLE);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Psilocybin mushrooms are wild ", NamedTextColor.WHITE),
                Component.text("or cultivated mushrooms that contain", NamedTextColor.WHITE),
                Component.text("psilocybin, a naturally-occurring", NamedTextColor.WHITE),
                Component.text("psychoactive and hallucinogenic compound.", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }

}
