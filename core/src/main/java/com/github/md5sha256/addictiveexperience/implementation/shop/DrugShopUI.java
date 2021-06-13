package com.github.md5sha256.addictiveexperience.implementation.shop;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.configuration.ShopConfiguration;
import com.github.md5sha256.addictiveexperience.util.AdventureUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@Singleton
public final class DrugShopUI {

    private final DrugRegistry drugRegistry;
    private final ShopConfiguration shopConfiguration;
    private final Economy economy;
    private final JavaPlugin plugin;
    private InventoryGui gui;

    @Inject
    public DrugShopUI(@NotNull JavaPlugin plugin,
                      @NotNull DrugRegistry drugRegistry,
                      @NotNull ShopConfiguration configuration,
                      @NotNull Economy economy) {
        this.plugin = plugin;
        this.drugRegistry = drugRegistry;
        this.shopConfiguration = configuration;
        this.economy = economy;
        this.gui = setup();
    }

    public void openShop(@NotNull HumanEntity entity) {
        this.gui.show(entity, true);
    }

    private @NotNull InventoryGui setup() {
        final String[] setup = new String[]{
                "#########",
                "#ddddddd#",
                "#ddddddd#",
                "#b##e##n#",
                "#########"
        };
        // D = drugs
        // # = panes
        // b = back, n = next, e = exit
        return new InventoryGui(this.plugin,
                                "&b&lDrug Shop",
                                setup,
                                elementDrugs('d'),
                                elementNext('n'),
                                elementPrevious('b'),
                                elementExit('e'));
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
                                  GuiPageElement.PageAction.NEXT,
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
                .color(displayName.color())
                .decorate(TextDecoration.BOLD)
                .build();
        final String price = String
                .format("Unit Price: %f", this.shopConfiguration.unitPrice(component));
        final List<Component> lore = Collections.singletonList(
                Component.text(price, NamedTextColor.AQUA, TextDecoration.BOLD)
        );
        AdventureUtils.setDisplayName(meta, buttonDisplayName);
        final String[] legacyLore = AdventureUtils.toLegacy(lore).toArray(new String[0]);
        button.setItemMeta(meta);
        return new StaticGuiElement(c, button, click -> {
            final HumanEntity clicked = click.getWhoClicked();
            if (!(clicked instanceof Player)) {
                return false;
            }
            TransactionType transactionType =
                    click.getType().isLeftClick() ? TransactionType.BUY : TransactionType.SELL;
            processTransaction((Player) clicked, transactionType, component, 1);
            return false;
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
            } else {
                String text = String
                        .format("You have bought %d of %s", amount, component.displayName());
                person.sendMessage(Component.text(text, NamedTextColor.GREEN));
            }
        } else {
            // FIXME implementation
        }
    }

    private enum TransactionType {
        BUY, SELL
    }

}
