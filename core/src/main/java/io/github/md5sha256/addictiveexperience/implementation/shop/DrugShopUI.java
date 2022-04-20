package io.github.md5sha256.addictiveexperience.implementation.shop;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import io.github.md5sha256.addictiveexperience.configuration.ShopConfiguration;
import com.github.md5sha256.spigotutils.AdventureUtils;
import com.google.inject.Inject;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@Singleton
public final class DrugShopUI {


    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("###.###");

    private final DrugRegistry drugRegistry;
    private final ShopConfiguration shopConfiguration;
    private final Economy economy;
    private final JavaPlugin plugin;
    private final IDrugForms drugForms;
    private InventoryGui gui;

    @Inject
    public DrugShopUI(@NotNull JavaPlugin plugin,
                      @NotNull DrugRegistry drugRegistry,
                      @NotNull IDrugForms drugForms,
                      @NotNull ShopConfiguration configuration,
                      @NotNull Economy economy) {
        this.plugin = plugin;
        this.drugRegistry = drugRegistry;
        this.drugForms = drugForms;
        this.shopConfiguration = configuration;
        this.economy = economy;
        setup();
    }

    public void openShop(@NotNull HumanEntity entity) {
        this.gui.show(entity, true);
    }

    public void setup() {
        final String[] setup = new String[]{
                "         ",
                " ddddddd ",
                " ddddddd ",
                " ddddddd ",
                " b  e  n ",
                "         "
        };
        // D = drugs
        // # = panes
        // b = back, n = next, e = exit
        this.gui = new InventoryGui(this.plugin,
                                    "&b&lDrug Shop",
                                    setup,
                                    elementPanes(' '),
                                    elementDrugs('d'),
                                    elementNext('n'),
                                    elementPrevious('b'),
                                    elementExit('e'));
    }

    private GuiElement elementPanes(char c) {
        final ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        final ItemMeta meta = itemStack.getItemMeta();
        AdventureUtils.setDisplayName(meta, Component.empty());
        itemStack.setItemMeta(meta);
        return new StaticGuiElement(c, itemStack);
    }

    private GuiElement elementExit(char c) {
        final ItemStack itemStack = new ItemStack(Material.BARRIER);
        final Component displayName = Component.text("Exit", NamedTextColor.RED);
        return new StaticGuiElement(c,
                                    itemStack,
                                    click -> {
                                        InventoryGui.getOpen(click.getWhoClicked()).close();
                                        return false;
                                    }, AdventureUtils.toLegacy(displayName));
    }

    private GuiElement elementNext(char c) {
        final ItemStack itemStack = new ItemStack(Material.PAPER);
        final Component displayName = Component.text("Next Page", NamedTextColor.DARK_AQUA);
        return new GuiPageElement(c,
                                  itemStack,
                                  GuiPageElement.PageAction.NEXT,
                                  AdventureUtils.toLegacy(displayName));
    }

    private GuiElement elementPrevious(char c) {
        final ItemStack itemStack = new ItemStack(Material.PAPER);
        final Component displayName = Component.text("Previous Page", NamedTextColor.DARK_AQUA);
        return new GuiPageElement(c,
                                  itemStack,
                                  GuiPageElement.PageAction.PREVIOUS,
                                  AdventureUtils.toLegacy(displayName));
    }

    private GuiElement elementDrugs(char c) {
        final GuiElementGroup element = new GuiElementGroup(c);
        for (IDrugComponent component : this.drugRegistry.components()) {
            element.addElement(elementDrug(component));
        }
        return element;
    }

    private GuiElement elementDrug(IDrugComponent component) {
        final char c = 'a';
        final ItemStack drugAsItem = component.asItem();
        final ItemStack button = new ItemStack(drugAsItem.getType());
        final ItemMeta meta = button.getItemMeta();
        final Component displayName = AdventureUtils.getDisplayName(meta);
        final Component buttonDisplayName = Component
                .text()
                .content("Buy " + component.displayName() + "!")
                .color(displayName == null ? NamedTextColor.GREEN : displayName.color())
                .build();

        final String price = String.format("- Unit Price: %s",
                                           PRICE_FORMAT.format(this.shopConfiguration
                                                                       .unitPrice(component)));
        final List<Component> text = Arrays.asList(
                buttonDisplayName,
                Component.text(price, NamedTextColor.GRAY)
        );
        AdventureUtils.setDisplayName(meta, buttonDisplayName);
        final String[] legacyLore = AdventureUtils.toLegacy(text).toArray(new String[0]);
        button.setItemMeta(meta);
        return new StaticGuiElement(c, button, click -> {
            final HumanEntity clicked = click.getWhoClicked();
            if (!(clicked instanceof Player)) {
                return true;
            }
            TransactionType transactionType =
                    click.getType().isLeftClick() ? TransactionType.BUY : TransactionType.SELL;
            processTransaction((Player) clicked, transactionType, component, 1);
            return true;
        }, legacyLore);
    }

    private void processTransaction(@NotNull Player person,
                                    @NotNull TransactionType transactionType,
                                    @NotNull IDrugComponent component,
                                    int amount) {
        if (transactionType == TransactionType.BUY) {
            double price = this.shopConfiguration.price(component, amount);
            final EconomyResponse response = this.economy.withdrawPlayer(person, price);
            if (!response.transactionSuccess()) {
                // FIXME send message
                person.sendMessage(Component.text("Insufficient funds", NamedTextColor.RED));
                InventoryGui.getOpen(person).close();
                return;
            }
            String text = String
                    .format("You have bought %d of %s", amount, component.displayName());
            person.sendMessage(Component.text(text, NamedTextColor.GREEN));
            final ItemStack itemStack;
            if (component instanceof IDrug) {
                final IDrugForm defaultForm = this.drugForms.powder();
                itemStack = this.drugRegistry.itemForDrug((IDrug) component, defaultForm);
            } else {
                itemStack = this.drugRegistry.itemForComponent(component);
            }
            itemStack.setAmount(amount);
            person.getInventory().addItem(itemStack);
        } else {
            // FIXME implementation
        }
    }

    private enum TransactionType {
        BUY, SELL
    }

}
