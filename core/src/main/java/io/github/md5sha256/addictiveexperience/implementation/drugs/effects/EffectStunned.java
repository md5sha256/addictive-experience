package io.github.md5sha256.addictiveexperience.implementation.drugs.effects;

import io.github.md5sha256.addictiveexperience.util.Utils;
import io.papermc.paper.event.entity.EntityMoveEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class EffectStunned extends SimpleCustomEffect implements Listener {

    private final Set<UUID> stunned = new HashSet<>();

    @Inject
    EffectStunned(@NotNull Plugin plugin, @NotNull PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin);
    }

    @Override
    public @NotNull String name() {
        return "stun effect";
    }

    @Override
    public @NotNull Component displayName() {
        return Component.text("stunned");
    }

    @Override
    public void applyTo(@NotNull LivingEntity entity) {
        this.stunned.add(entity.getUniqueId());
    }

    @Override
    public void removeFrom(@NotNull LivingEntity entity) {
        this.stunned.remove(entity.getUniqueId());
    }

    @Override
    public long duration(@NotNull TimeUnit timeUnit) {
        return TimeUnit.SECONDS.convert(10, timeUnit);
    }

    @Override
    public @NotNull Key key() {
        return Utils.internalKey("effect-stun");
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityMove(@NotNull EntityMoveEvent event) {
        if (!this.stunned.contains(event.getEntity().getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        event.getEntity().sendActionBar(Component.text("You cannot move; you are stunned!"));
    }
}
