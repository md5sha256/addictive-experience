package io.github.md5sha256.addictiveexperience.implementation.effects;

import io.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import io.github.md5sha256.addictiveexperience.api.effect.EffectData;
import io.github.md5sha256.addictiveexperience.api.effect.IEffectHandler;
import com.github.md5sha256.spigotutils.Common;
import com.github.md5sha256.spigotutils.serial.Registry;
import com.github.md5sha256.spigotutils.serial.SimpleRegistry;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public class SimpleEffectHandler implements IEffectHandler {

    private final CustomEffectResolver resolver;
    private final Registry<Key, CustomEffect> enchantmentRegistry = new SimpleRegistry<>();
    private final Map<LivingEntity, Map<Key, EffectData>> enchantmentCache = new HashMap<>();
    private final BukkitTask task;

    @Inject
    public SimpleEffectHandler(@NotNull Plugin plugin,
                               @NotNull BukkitScheduler scheduler,
                               @NotNull CustomEffectResolver resolver) {
        long ticks = Common.toTicks(1, TimeUnit.MINUTES);
        this.task = scheduler.runTaskTimer(plugin, (Runnable) this::saveData, ticks, ticks);
        this.resolver = resolver;
    }

    private @NotNull EffectData createData(long expectedDuration) {
        return new SimpleEffectData(Stopwatches.variableStopwatch(Stopwatches.newInstance()), expectedDuration);
    }

    public void shutdown() {
        if (!this.task.isCancelled()) {
            this.task.cancel();
            saveData();
            unregisterEvents();
        }
    }

    @Override
    public void update() {
        for (Map.Entry<LivingEntity, Map<Key, EffectData>> entry : this.enchantmentCache.entrySet()) {
            entry.getValue().entrySet().removeIf(e -> {
                if (e.getValue().remainingDurationMillis() == 0) {
                    removeEffect(entry.getKey(), e.getKey());
                    return true;
                }
                return false;
            });
        }
        this.enchantmentCache.values().removeIf(Map::isEmpty);
    }

    @Override
    public void update(@NotNull LivingEntity entity) {
        final Map<Key, EffectData> data = this.enchantmentCache.get(entity);
        if (data != null) {
            data.entrySet().removeIf(e -> {
                if (e.getValue().remainingDurationMillis() == 0) {
                    removeEffect(entity, e.getKey());
                    return true;
                }
                return false;
            });
            if (data.isEmpty()) {
                this.enchantmentCache.remove(entity);
            }
        }
    }

    @Override
    public void saveData() {
        update();
        for (Map.Entry<LivingEntity, Map<Key, EffectData>> entry : this.enchantmentCache.entrySet()) {
            this.resolver.saveData(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void saveData(@NotNull LivingEntity entity) {
        update(entity);
        this.resolver.saveData(entity,
                this.enchantmentCache.getOrDefault(entity, Collections.emptyMap()));
    }

    @Override
    public void loadData(@NotNull LivingEntity entity) {
        this.enchantmentCache.put(entity, this.resolver.loadData(entity));
    }

    @Override
    public @NotNull Registry<Key, CustomEffect> registry() {
        return this.enchantmentRegistry;
    }

    @Override
    public void applyEffect(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect customEffect
    ) {
        applyEffect(entity, customEffect, customEffect.durationMillis());
    }

    @Override
    public void applyEffect(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect customEffect,
            long durationMillis)
    {
        customEffect.applyTo(entity);
        final Map<Key, EffectData> data = this.enchantmentCache
                .computeIfAbsent(entity, x -> new HashMap<>());
        data.put(customEffect.key(), createData(durationMillis));
        this.resolver.saveData(entity, data);
    }

    @Override
    public void removeEffect(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect customEffect
    ) {
        customEffect.removeFrom(entity);
    }

    private void removeEffect(@NotNull LivingEntity entity, @NotNull Key key) {
        this.enchantmentRegistry.get(key).ifPresent(enchantment -> enchantment.removeFrom(entity));
    }

    @Override
    public @NotNull Collection<CustomEffect> enchantments(@NotNull LivingEntity entity) {
        Map<Key, EffectData> dataMap = this.enchantmentCache.get(entity);
        if (dataMap == null) {
            loadData(entity);
            dataMap = this.enchantmentCache.get(entity);
        }
        Collection<CustomEffect> enchantments = new HashSet<>(dataMap.size());
        for (Key key : dataMap.keySet()) {
            this.enchantmentRegistry.get(key).ifPresent(enchantments::add);
        }
        return enchantments;
    }

    @Override
    public @NotNull Optional<@NotNull EffectData> enchantmentData(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect enchantment
    ) {
        if (!this.enchantmentCache.containsKey(entity)) {
            loadData(entity);
        }
        return Optional.ofNullable(this.enchantmentCache.get(entity))
                .flatMap(map -> Optional.ofNullable(map.get(enchantment.key())));
    }

    @Override
    public boolean hasEnchantment(@NotNull LivingEntity entity, @NotNull CustomEffect enchantment) {
        return hasEnchantment(entity, enchantment.key());
    }

    @Override
    public boolean hasEnchantment(@NotNull LivingEntity entity, @NotNull Key key) {
        return this.enchantmentCache.getOrDefault(entity, Collections.emptyMap()).containsKey(key);
    }

}
