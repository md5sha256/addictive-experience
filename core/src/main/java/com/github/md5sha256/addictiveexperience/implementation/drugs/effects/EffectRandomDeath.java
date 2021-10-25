package com.github.md5sha256.addictiveexperience.implementation.drugs.effects;

import com.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EffectRandomDeath extends EffectRandomAction {

    private final Plugin plugin;

    @Inject
    EffectRandomDeath(
            @NotNull Plugin plugin,
            @NotNull Server server,
            @NotNull BukkitScheduler scheduler) {
        super(
                server,
                scheduler,
                new NamespacedKey(plugin, "random-death"),
                "Random Death",
                Component.text("Random Death")
        );
        this.plugin = plugin;
    }

    public @NotNull CustomEffect createEffect(
            long intervalTicks,
            long durationMillis,
            double chance
    ) {
        return new EffectImpl(this.plugin, intervalTicks, durationMillis, chance);
    }

    @Override
    public void onPresentInInventory(@NotNull InventoryHolder inventoryHolder) {

    }

    @Override
    public void onRemovalFromInventory(@NotNull InventoryHolder inventoryHolder) {

    }

    @Override
    public void onPresentInEquipmentSlot(@NotNull EquipmentSlot equipmentSlot) {

    }

    @Override
    public void onRemovalFromEquipmentSlot(@NotNull EquipmentSlot equipmentSlot) {

    }

    private class EffectImpl extends BaseImpl {

        public EffectImpl(@NotNull Plugin plugin,
                          long intervalTicks,
                          long durationMillis,
                          double chance) {
            super(plugin, intervalTicks, durationMillis, chance);
        }

        @Override
        protected void activate(@NotNull LivingEntity livingEntity) {
            // Effectively kill
            livingEntity.remove();
        }

    }


}
