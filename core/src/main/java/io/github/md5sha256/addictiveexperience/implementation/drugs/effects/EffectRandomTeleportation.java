package io.github.md5sha256.addictiveexperience.implementation.drugs.effects;

import io.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EffectRandomTeleportation extends EffectRandomAction {
    
    private final Plugin plugin;

    @Inject
    EffectRandomTeleportation(
            @NotNull Plugin plugin,
            @NotNull Server server,
            @NotNull BukkitScheduler scheduler) {
        super(
                server,
                scheduler,
                new NamespacedKey(plugin, "random-teleportation"),
                "Random Teleportation",
                Component.text("Random Teleportation")
        );
        this.plugin = plugin;
    }

    public @NotNull CustomEffect createEffect(
            long intervalTicks,
            long durationMillis,
            double chance,
            int teleportationRadius
    ) {
        return new EffectImpl(this.plugin, intervalTicks, durationMillis, chance, teleportationRadius);
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

        private final double halfRadius;

        public EffectImpl(@NotNull Plugin plugin,
                          long intervalTicks,
                          long durationMillis,
                          double chance,
                          int radius) {
            super(plugin, intervalTicks, durationMillis, chance);
            this.halfRadius = radius / 2D;
        }

        private double generateRandomCoordinate(double existing) {
            final double distance = EffectRandomTeleportation.this.random.nextDouble() * this.halfRadius;
            final boolean positive = EffectRandomTeleportation.this.random.nextBoolean();
            return positive ? existing + distance : existing - distance;
        }

        @Override
        protected void activate(@NotNull LivingEntity livingEntity) {
            final Location location = livingEntity.getLocation();
            final double x = generateRandomCoordinate(location.getX());
            final double y = generateRandomCoordinate(location.getY());
            final double z = generateRandomCoordinate(location.getZ());
            location.set(x, y, z);
            livingEntity.teleportAsync(location);
        }
    }


}
