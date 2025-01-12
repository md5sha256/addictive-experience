package io.github.md5sha256.addictiveexperience.implementation.forms;

import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.AbstractDrugForm;
import io.github.md5sha256.addictiveexperience.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
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

@Singleton
public final class FormSyringe extends AbstractDrugForm implements IDrugForm {

    private final Material material = Material.GOLDEN_HOE;
    private final ItemMeta emptySyringe = emptySyringe();
    private final Plugin plugin;
    private final Server server;
    private final DrugRegistry registry;
    private final ItemStack unfilledItem;

    @Inject
    FormSyringe(@NotNull Plugin plugin,
                @NotNull Server server,
                @NotNull DrugRegistry registry,
                @NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("syringe"), "Syringe");
        this.plugin = plugin;
        this.server = server;
        this.registry = registry;
        this.unfilledItem = createUnfilled();
        registerUnfilledSyringe();
    }

    private ItemStack createUnfilled() {
        final ItemStack itemStack = ItemStack.of(this.material);
        asMeta().ifPresent(itemStack::setItemMeta);
        return itemStack;
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.of(this.unfilledItem.clone());
    }

    public void registerUnfilledSyringe() {
        final NamespacedKey key = new NamespacedKey(this.plugin, "unfilled-syringe");
        final ShapedRecipe recipe = new ShapedRecipe(key, this.unfilledItem);
        recipe.shape("$$$", " % ", " % ");
        recipe.setIngredient('$', Material.STICK);
        recipe.setIngredient('%', Material.GLASS);
        this.server.addRecipe(recipe);
    }

    private String toValue(Key key) {
        return key.toString().replace(":", "_");
    }

    public void registerFilledSyringeRecipe(@NotNull IDrug drug) {
        final NamespacedKey key = new NamespacedKey(this.plugin,
                "filled-syringe" + toValue(drug.key()));
        ItemStack drugItem = drug.asItem(this.registry);
        final ShapedRecipe recipe = new ShapedRecipe(key, this.registry.itemForDrug(drug, this));
        recipe.shape("%$ ", "   ", "   ");
        recipe.setIngredient('$', new RecipeChoice.ExactChoice(drugItem));
        recipe.setIngredient('%', this.unfilledItem);
        this.server.addRecipe(recipe);
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
