package com.github.md5sha256.addictiveexperience.implementation;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.github.md5sha256.addictiveexperience.implementation.forms.FormPowder;
import com.github.md5sha256.addictiveexperience.util.Utils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

public class FormHandlerPowder extends AbstractFormHandler implements Listener {

    private final FormPowder form;

    public FormHandlerPowder(@NotNull Plugin plugin,
                             @NotNull PluginManager pluginManager,
                             @NotNull DrugHandler handler,
                             @NotNull DrugRegistry registry,
                             @NotNull SlurEffectState effectState,
                             @NotNull FormPowder form) {
        super(plugin, registry, handler, effectState);
        this.form = form;
        pluginManager.registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrugUse(PlayerInteractEvent event) {
        final EquipmentSlot hand = event.getHand();
        final Action action = event.getAction();
        if ((hand != EquipmentSlot.HAND && hand != EquipmentSlot.OFF_HAND)
                || (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR)) {
            return;
        }
        final Player player = event.getPlayer();
        final ItemStack used = event.getItem();
        if (used == null) {
            return;
        }
        final Optional<DrugItemData> optionalData = this.drugRegistry.dataFor(used);
        if (!optionalData.isPresent()) {
            return;
        }
        event.setCancelled(true);
        final DrugItemData data = optionalData.get();
        if (this.form == data.form()) {
            handlePlayerDrugUse(player, hand, used, data);
        }
    }

    @Override
    protected void sendMessageOnItemUse(@NotNull Player player, @NotNull DrugItemData used) {
        final String messagePath = String.format("Snort%s", Utils.capitalise(used.drug().identifierName().toLowerCase(Locale.ENGLISH)));
        final String message = this.plugin.getConfig().getString(messagePath);
        if (message != null) {
            player.sendMessage(Utils.legacyColorize(message));
        }
    }

}
