package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics.ecstasy.components;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugComponent;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Mercury extends AbstractDrugComponent {

    @Inject
    Mercury(@NotNull ItemFactory itemFactory, DrugRegistry registry) {
        super(itemFactory, Utils.internalKey("mercury"), "Mercury", Material.GUNPOWDER);
        registry.registerComponent(this);
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.GUNPOWDER);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        AdventureUtils.setDisplayName(meta, Component.text("mercury", NamedTextColor.GOLD));
        List<Component> lore = Arrays.asList(
                Component.text("Highly toxic element, which is", NamedTextColor.WHITE),
                Component.text("used to create Ecstasy", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.empty();
    }
}
