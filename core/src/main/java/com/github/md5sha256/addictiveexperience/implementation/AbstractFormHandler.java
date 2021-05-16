package com.github.md5sha256.addictiveexperience.implementation;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugCooldownData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.github.md5sha256.addictiveexperience.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractFormHandler {

    protected final Plugin plugin;
    protected final DrugRegistry drugRegistry;
    protected final DrugHandler drugHandler;
    protected final SlurEffectState slurEffectState;

    protected AbstractFormHandler(@NotNull Plugin plugin,
                                  @NotNull DrugRegistry drugRegistry,
                                  @NotNull DrugHandler drugHandler,
                                  @NotNull SlurEffectState effectState) {
        this.plugin = Objects.requireNonNull(plugin);
        this.drugHandler = Objects.requireNonNull(drugHandler);
        this.drugRegistry = Objects.requireNonNull(drugRegistry);
        this.slurEffectState = Objects.requireNonNull(effectState);
    }

    protected void checkPermissions(@NotNull CommandSender sender, @NotNull DrugItemData itemData) {
        final IDrug drug = itemData.drug();
        if (!sender.hasPermission(drug.permission())) {
            sender.sendMessage(Utils.legacyColorize(this.plugin.getConfig().getString("nopermstoconsume")));
        }
    }

    protected abstract void sendMessageOnItemUse(@NotNull Player player, @NotNull DrugItemData used);

    protected void handleDrugUse(@NotNull LivingEntity entity,
                                 @NotNull ItemStack itemStack,
                                 @NotNull DrugItemData itemData
    ) {
        checkPermissions(entity, itemData);
        final IDrug drug = itemData.drug();
        final IDrugForm drugForm = itemData.form();
        itemStack.setAmount(itemStack.getAmount() - 1);
        this.drugRegistry.metaData(drug, DrugMeta.KEY)
                         .map(DrugMeta::effects)
                         .ifPresent(entity::addPotionEffects);
        this.drugHandler.bloodData(entity.getUniqueId())
                        .ifPresent(bloodData -> bloodData.incrementLevel(drug, 10));
        this.drugHandler.notifyIfOverdosed(entity, drug);
        this.slurEffectState.registerSlur(entity.getUniqueId(), drug);
        final UUID playerUID = entity.getUniqueId();
        final DrugCooldownData cooldownData = this.drugHandler.cooldownData();
        cooldownData.setBlocked(playerUID, drug, drugForm);
        scheduleTask(() -> cooldownData.setUnblocked(playerUID, drug, drugForm), 20);
    }

    protected void handlePlayerDrugUse(@NotNull Player player, @NotNull EquipmentSlot equipmentSlot,
                                       @NotNull ItemStack itemStack, @NotNull DrugItemData itemData) {
        checkPermissions(player, itemData);
        handleDrugUse(player, itemStack, itemData);
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.getInventory().setItem(equipmentSlot, itemStack);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.6f);
        sendMessageOnItemUse(player, itemData);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            // if (this.plugin.acs)
            if (true) {
                player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CELEBRATE, 0.6f, 1.0f);
            }
        }, 8L);
    }

    protected void scheduleTask(@NotNull Runnable task, long delayTicks) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, task, delayTicks);
    }

}
