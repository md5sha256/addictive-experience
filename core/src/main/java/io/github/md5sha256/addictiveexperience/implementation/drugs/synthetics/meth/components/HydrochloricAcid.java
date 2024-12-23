package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.meth.components;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
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
public final class HydrochloricAcid extends AbstractDrugComponent {

    @Inject
    HydrochloricAcid(@NotNull ItemFactory itemFactory, DrugRegistry registry) {
        super(itemFactory, Utils.internalKey("hydrochloric-acid"), "Hydrochloric Acid", Material.WATER_BUCKET);
        registry.registerComponent(this);
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.WATER_BUCKET);
        final Component displayName = Component.text("Hydrochloric acid", NamedTextColor.BLUE);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Used for the production", NamedTextColor.WHITE),
                Component.text("of Methamphetamine!", NamedTextColor.WHITE),
                Component.text("- Can be thrown on other players", NamedTextColor.WHITE),
                Component.text("and entities!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.empty();
    }
}
