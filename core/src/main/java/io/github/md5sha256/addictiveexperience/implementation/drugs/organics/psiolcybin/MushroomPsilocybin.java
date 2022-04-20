package io.github.md5sha256.addictiveexperience.implementation.drugs.organics.psiolcybin;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IOrganic;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrug;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MushroomPsilocybin extends AbstractDrug implements IOrganic {

    private final DrugMeta defaultMeta;

    @Inject
    public MushroomPsilocybin(@NotNull ItemFactory itemFactory,
                              @NotNull DrugRegistry drugRegistry) {
        super(itemFactory,
              Utils.internalKey("mushroom_psilocybin"),
              "Psilocybin Mushroom",
              Material.BROWN_MUSHROOM,
              "addictiveexperience.consumepsilocybin");
        this.defaultMeta = DrugMeta.DEFAULT
                .toBuilder()
                .slurEffect(null)
                .potionEffects(
                        new PotionEffect(PotionEffectType.CONFUSION, 100, 2),
                        new PotionEffect(PotionEffectType.SPEED, 500, 5)
                )
                .overdoseThreshold(200)
                .build();
        drugRegistry.registerComponent(this);
        drugRegistry.metaData(this, DrugPlantMeta.KEY, DrugPlantMeta.defaultMeta(this, this));
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

    @Override
    public @NotNull DrugMeta defaultMeta() {
        return this.defaultMeta;
    }
}
