package com.github.md5sha256.addictiveexperience.implementation.forms.blunt;

import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.forms.BluntState;
import com.github.md5sha256.addictiveexperience.api.forms.FormBlunt;
import com.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import com.github.md5sha256.addictiveexperience.util.Utils;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
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

public class BluntLit extends AbstractDrugForm implements FormBlunt {

    @Inject
    BluntLit(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("blunt_lit"), "Blunt_Lit");
    }

    @Override
    public @NotNull BluntState bluntState() {
        return BluntState.LIT;
    }

    private @NotNull ItemMeta metaFilled(@NotNull String contents) {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.TORCH);
        final Component displayName =
                Component.text("Blunt", NamedTextColor.DARK_GRAY)
                         .append(Component.space())
                         .append(Component.text("<Lit>", NamedTextColor.RED));
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text(String.format("Contents: %s", contents), NamedTextColor.WHITE),
                Component.space(),
                Component.text("Right Click to hit a blunt!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull final IDrug drug) {
        final ItemStack itemStack = new ItemStack(Material.TORCH);
        itemStack.setItemMeta(asMeta(drug));
        return itemStack;
    }

    @Override
    public @NotNull ItemMeta asMeta(@NotNull final IDrug drug) {
        return metaFilled(drug.displayName());
    }

    @Override
    public @NotNull Optional<@NotNull ItemMeta> asMeta() {
        return Optional.empty();
    }

}
