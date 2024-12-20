package io.github.md5sha256.addictiveexperience.implementation.forms;

import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class FormSyringe extends AbstractDrugForm implements IDrugForm {

    private final Material material = Material.GOLDEN_HOE;
    private final ItemMeta emptySyringe = emptySyringe();

    @Inject
    FormSyringe(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("syringe"), "Syringe");
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        final ItemStack itemStack = ItemStack.of(this.material);
        asMeta().ifPresent(itemStack::setItemMeta);
        return Optional.of(itemStack);
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull IDrug drug) {
        final ItemStack item = ItemStack.of(this.material);
        item.setItemMeta(asMeta(drug));
        return item;
    }

    @Override
    public @NotNull ItemMeta asMeta(@NotNull IDrug drug) {
        return filledSyringe(drug);
    }

    @Override
    public @NotNull Optional<@NotNull ItemMeta> asMeta() {
        return Optional.of(this.emptySyringe.clone());
    }

    private Component syringeDisplayName(@NotNull String type) {
        return Component.text("Syringe", NamedTextColor.LIGHT_PURPLE)
                        .append(Component.text("<", NamedTextColor.DARK_RED))
                        .append(Component.text(type, NamedTextColor.DARK_GRAY))
                        .append(Component.text(">", NamedTextColor.DARK_RED));
    }

    private @NotNull ItemMeta emptySyringe() {
        final ItemMeta meta = this.itemFactory.getItemMeta(this.material);
        final Component displayName = syringeDisplayName("EMPTY");
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Drugs can be inserted into", NamedTextColor.WHITE),
                Component.text("a syringe to boost their effects!", NamedTextColor.WHITE),
                Component.empty(),
                Component.text("To insert a drug: Have the drug", NamedTextColor.WHITE),
                Component.text("on your cursor, and hover over an empty", NamedTextColor.WHITE),
                Component.text("syringe, then left click to put it in!", NamedTextColor.WHITE),
                Component.empty(),
                Component.text("To remove a drug: Simply right click", NamedTextColor.WHITE),
                Component.text("the filled syringe to get the drug back!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    private @NotNull ItemMeta filledSyringe(@NotNull IDrug drug) {
        final ItemMeta meta = this.itemFactory.getItemMeta(this.material);
        final Component displayName = syringeDisplayName(drug.displayName());
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text(String.format("Right click to inject %s!", drug.displayName()),
                               NamedTextColor.WHITE),
                Component.empty(),
                Component.text("Left click to remove ", NamedTextColor.WHITE),
                Component.text(String.format("%s from Syringe!", drug.displayName()),
                               NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

}
