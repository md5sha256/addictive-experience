package io.github.md5sha256.addictiveexperience.implementation.drugs.organics.marijuana.components;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import io.github.md5sha256.addictiveexperience.util.Utils;
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
public final class PlantMarijuana extends AbstractDrugComponent implements IDrugComponent {

    @Inject
    PlantMarijuana(@NotNull ItemFactory itemFactory, @NotNull DrugRegistry drugRegistry) {
        super(itemFactory,
              Utils.internalKey("plant_marijuana"),
              "Marijuana Plant",
              Material.LARGE_FERN);
        drugRegistry.registerComponent(this);
    }

    protected final @NotNull ItemMeta meta() {
        ItemMeta meta = this.itemFactory.getItemMeta(Material.LARGE_FERN);
        final Component displayName = Component.text("Marijuana Plant", NamedTextColor.GREEN);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Used to harvest", NamedTextColor.WHITE),
                Component.text("marijuana", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }
}
