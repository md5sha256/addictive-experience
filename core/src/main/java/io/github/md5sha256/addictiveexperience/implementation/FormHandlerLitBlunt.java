package io.github.md5sha256.addictiveexperience.implementation;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.effect.IEffectHandler;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import io.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
import jakarta.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FormHandlerLitBlunt extends AbstractFormHandler implements Listener {

    private final DrugForms drugForms;

    @Inject
    FormHandlerLitBlunt(@NotNull Plugin plugin,
                        @NotNull PluginManager pluginManager,
                        @NotNull DrugRegistry drugRegistry,
                        @NotNull DrugHandler drugHandler,
                        @NotNull SlurEffectState effectState,
                        @NotNull IEffectHandler effectHandler,
                        @NotNull DrugForms forms
    ) {
        super(plugin, drugRegistry, drugHandler, effectState, effectHandler);
        this.drugForms = forms;
        pluginManager.registerEvents(this, plugin);
    }


    @EventHandler(ignoreCancelled = true)
    public void processBluntLit(@NotNull InventoryClickEvent clickEvent) {
        if (!clickEvent.isRightClick()
                || clickEvent.getCursor().getType() != Material.FLINT_AND_STEEL
                || clickEvent.getCurrentItem() == null
                || clickEvent.getClickedInventory() == null) {
            return;
        }
        ItemStack currentItem = clickEvent.getCurrentItem();
        Optional<DrugItemData> optional = this.drugRegistry.dataFor(currentItem);
        if (optional.isEmpty()) {
            return;
        }
        DrugItemData itemData = optional.get();
        if (!itemData.form().equals(this.drugForms.blunt().unlit())) {
            return;
        }
        ItemStack litItem = drugRegistry.itemForDrug(itemData.drug(), this.drugForms.blunt().lit());
        if (currentItem.getAmount() == 1) {
            clickEvent.setCurrentItem(litItem);
            return;
        }
        currentItem.subtract(1);
        clickEvent.setCurrentItem(currentItem);
        clickEvent.getClickedInventory().addItem(litItem);
    }

    @EventHandler(ignoreCancelled = true)
    public void processBluntUse(final PlayerInteractEvent e) {
        if ((e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
                || e.getHand() != EquipmentSlot.HAND
        ) {
            return;
        }
        final ItemStack used = e.getItem();
        if (used == null) {
            return;
        }
        Optional<DrugItemData> optional = drugRegistry.dataFor(used);
        if (optional.isEmpty()) {
            return;
        }
        DrugItemData itemData = optional.get();
        if (!itemData.form().equals(drugForms.blunt().lit())) {
            return;
        }
        final Player p = e.getPlayer();
        if (!p.hasPermission("addictiveexperience.consumeweed")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ("ftoconsume")));
            e.setCancelled(true);
            return;
        }
        handlePlayerDrugUse(p, e.getHand(), used, itemData);
        e.setCancelled(true);
    }

    @Override
    protected void playSounds(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_BURN, 1.0f, 0.7f);
    }

    @Override
    protected void sendMessageOnItemUse(@NotNull Player player, @NotNull DrugItemData used) {
    }
}
