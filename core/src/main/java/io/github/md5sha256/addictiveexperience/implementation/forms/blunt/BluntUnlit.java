package io.github.md5sha256.addictiveexperience.implementation.forms.blunt;

import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.BluntState;
import io.github.md5sha256.addictiveexperience.api.forms.FormBlunt;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import io.github.md5sha256.addictiveexperience.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class BluntUnlit extends AbstractDrugForm implements FormBlunt {

    private final ItemStack itemEmptyContents;
    private final ItemMeta metaEmptyContents;

    @Inject
    BluntUnlit(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("blunt_unlit"), "Blunt_Unlit");
        this.metaEmptyContents = metaEmpty();
        this.itemEmptyContents = ItemStack.of(Material.STICK);
        this.itemEmptyContents.setItemMeta(this.metaEmptyContents);
    }

    private String toValue(Key key) {
        return key.toString().replace(":", "_");
    }

    public void registerUnlitBluntRecipe(@NotNull Plugin plugin, @NotNull DrugRegistry registry, @NotNull IDrug drug) {
        final ItemStack emptyBlunt = registry.itemForDrug(drug, this);
        // FIXME debug the empty blunt item
        final ItemStack drugItem = registry.itemForComponent(drug);
        final NamespacedKey key1 = new NamespacedKey(plugin, "unlit-blunt-" + toValue(drug.key()));
        final ShapedRecipe recipe1 = new ShapedRecipe(key1, emptyBlunt);
        recipe1.shape("$$$", "%%%", "   ");
        recipe1.setIngredient('$', new RecipeChoice.ExactChoice(drugItem));
        recipe1.setIngredient('%', Material.PAPER);

        final NamespacedKey key2 = new NamespacedKey(plugin,
                "unlit-blunt-alt" + toValue(drug.key()));
        final ShapedRecipe recipe2 = new ShapedRecipe(key2, emptyBlunt);
        recipe2.shape("   ", "%%%", "$$$");
        recipe2.setIngredient('$', new RecipeChoice.ExactChoice(drugItem));
        recipe2.setIngredient('%', Material.PAPER);

        plugin.getServer().addRecipe(recipe1);
        plugin.getServer().addRecipe(recipe2);
    }

    @Override
    public @NotNull BluntState bluntState() {
        return BluntState.UNLIT;
    }

    private @NotNull ItemMeta metaEmpty() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.STICK);
        final Component displayName =
                Component.text("Blunt", NamedTextColor.DARK_GRAY)
                        .append(Component.space())
                        .append(Component.text("<Not Lit>", NamedTextColor.DARK_GRAY));

        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Right click with flint and steel", NamedTextColor.WHITE),
                Component.text("to light the blunt!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    private @NotNull ItemMeta metaFilled(@NotNull String contents) {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.STICK);
        final Component displayName =
                Component.text("Blunt", NamedTextColor.DARK_GRAY)
                        .append(Component.space())
                        .append(Component.text("<Not Lit>", NamedTextColor.DARK_GRAY));

        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text(String.format("Contents: %s", contents)),
                Component.space(),
                Component.text("Right click with flint and steel", NamedTextColor.WHITE),
                Component.text("to light the blunt!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.of(this.itemEmptyContents);
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull final IDrug drug) {
        final ItemStack itemStack = ItemStack.of(Material.STICK);
        itemStack.setItemMeta(asMeta(drug));
        return itemStack;
    }

    @Override
    public @NotNull ItemMeta asMeta(@NotNull final IDrug drug) {
        return metaFilled(drug.displayName());
    }

    @Override
    public @NotNull Optional<@NotNull ItemMeta> asMeta() {
        return Optional.of(this.metaEmptyContents);
    }
}
