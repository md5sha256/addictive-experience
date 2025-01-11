package io.github.md5sha256.addictiveexperience.implementation;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugBloodData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.effect.IEffectHandler;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import io.github.md5sha256.addictiveexperience.implementation.forms.FormSyringe;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.google.inject.Inject;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class FormHandlerSyringe extends AbstractFormHandler implements Listener {

    private final FormSyringe form;

    @Inject
    public FormHandlerSyringe(@NotNull Plugin plugin,
                              @NotNull PluginManager pluginManager,
                              @NotNull DrugRegistry drugRegistry,
                              @NotNull DrugHandler drugHandler,
                              @NotNull SlurEffectState slurEffectState,
                              @NotNull IEffectHandler effectHandler,
                              @NotNull FormSyringe form) {
        super(plugin, drugRegistry, drugHandler, slurEffectState, effectHandler);
        this.form = form;
        pluginManager.registerEvents(this, plugin);
    }

    @Override
    protected void sendMessageOnItemUse(@NotNull Player player, @NotNull DrugItemData used) {
    }

    @EventHandler
    public void handleSyringeInjection(final EntityDamageByEntityEvent event) {
        final Entity entityDamager = event.getDamager();
        final Entity entityTarget = event.getEntity();
        if (!(entityDamager instanceof final Player damager) || !(entityTarget instanceof final LivingEntity target)) {
            return;
        }

        final PlayerInventory inventory = damager.getInventory();

        final ItemStack inMainHand = inventory.getItemInMainHand();
        final Optional<DrugItemData> optionalItemData = this.drugRegistry.dataFor(inMainHand);
        if (optionalItemData.isEmpty()) {
            return;
        }
        final DrugItemData data = optionalItemData.get();
        if (this.form != data.form()) {
            return;
        }
        event.setCancelled(true);
        // The target gets the drug effects
        handleDrugUse(target, inMainHand, data);
        final IDrug component = data.drug();
        inMainHand.setAmount(inMainHand.getAmount() - 1);
        damager.getInventory().setItemInMainHand(inMainHand);
        damager.playSound(damager.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 0.6f);
        final String messagePathAttacker = String.format("Inject%sAttacker",
                component.displayName());
        final String messagePathVictim = String.format("Inject%sVictim", component.displayName());
        // Send messages to both players here

        // FIXME prefix
        damager.sendMessage("prefix" + Utils
                .legacyColorize(plugin.getConfig().getString(messagePathAttacker)));
        target.sendMessage("prefix" + Utils
                .legacyColorize(plugin.getConfig().getString(messagePathVictim)));
        // Add potion effects
        this.drugRegistry.metaData(component, DrugMeta.KEY).map(DrugMeta::potionEffects)
                .ifPresent(target::addPotionEffects);
        // Manually update blood
        final DrugBloodData drugBloodData = this.drugHandler
                .getOrCreateBloodData(target.getUniqueId());
        drugBloodData.incrementLevel(component, 10);
        this.drugHandler.notifyIfOverdosed(target, component);
        this.slurEffectState.registerSlur(target.getUniqueId(), component);
    }

}
