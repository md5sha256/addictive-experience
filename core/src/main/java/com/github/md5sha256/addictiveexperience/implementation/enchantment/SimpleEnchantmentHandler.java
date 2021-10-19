package com.github.md5sha256.addictiveexperience.implementation.enchantment;

import com.github.md5sha256.addictiveexperience.api.enchantment.CustomEnchantment;
import com.github.md5sha256.addictiveexperience.api.enchantment.EnchantmentData;
import com.github.md5sha256.addictiveexperience.api.enchantment.IEnchantmentHandler;
import com.github.md5sha256.spigotutils.Common;
import com.github.md5sha256.spigotutils.serial.Registry;
import com.github.md5sha256.spigotutils.serial.SimpleRegistry;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
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
public class SimpleEnchantmentHandler implements IEnchantmentHandler {

    private final CustomEnchantmentResolver resolver;
    private final Registry<Key, CustomEnchantment> enchantmentRegistry = new SimpleRegistry<>();
    private final Map<Entity, Map<Key, EnchantmentData>> enchantmentCache = new HashMap<>();
    private final BukkitTask task;

    @Inject
    public SimpleEnchantmentHandler(@NotNull Plugin plugin,
                                    @NotNull BukkitScheduler scheduler,
                                    @NotNull CustomEnchantmentResolver resolver) {
        long ticks = Common.toTicks(1, TimeUnit.MINUTES);
        this.task = scheduler.runTaskTimer(plugin, (Runnable) this::saveData, ticks, ticks);
        this.resolver = resolver;
    }

    private @NotNull EnchantmentData createData(long expectedDuration) {
        return new SimpleEnchantmentData(Stopwatches.variableStopwatch(Stopwatches.newInstance()), expectedDuration);
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
        for (Map.Entry<Entity, Map<Key, EnchantmentData>> entry : this.enchantmentCache.entrySet()) {
            entry.getValue().entrySet().removeIf(e -> {
                if (e.getValue().remainingDurationMillis() == 0) {
                    removeEnchantment(entry.getKey(), e.getKey());
                    return true;
                }
                return false;
            });
        }
        this.enchantmentCache.values().removeIf(Map::isEmpty);
    }

    @Override
    public void update(@NotNull Entity entity) {
        final Map<Key, EnchantmentData> data = this.enchantmentCache.get(entity);
        if (data != null) {
            data.entrySet().removeIf(e -> {
                if (e.getValue().remainingDurationMillis() == 0) {
                    removeEnchantment(entity, e.getKey());
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
        for (Map.Entry<Entity, Map<Key, EnchantmentData>> entry : this.enchantmentCache.entrySet()) {
            this.resolver.saveData(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void saveData(@NotNull Entity holder) {
        update(holder);
        this.resolver.saveData(holder,
                this.enchantmentCache.getOrDefault(holder, Collections.emptyMap()));
    }

    @Override
    public void loadData(@NotNull Entity holder) {
        this.enchantmentCache.put(holder, this.resolver.loadData(holder));
    }

    @Override
    public @NotNull Registry<Key, CustomEnchantment> registry() {
        return this.enchantmentRegistry;
    }

    @Override
    public void applyEnchantment(
            @NotNull Entity entity,
            @NotNull CustomEnchantment customEnchantment
    ) {
        applyEnchantment(entity, customEnchantment, customEnchantment.durationMillis());
    }

    @Override
    public void applyEnchantment(
            @NotNull Entity entity,
            @NotNull CustomEnchantment customEnchantment,
            long durationMillis)
    {
        customEnchantment.applyTo(entity);
        final Map<Key, EnchantmentData> data = this.enchantmentCache
                .computeIfAbsent(entity, x -> new HashMap<>());
        data.put(customEnchantment.key(), createData(durationMillis));
        this.resolver.saveData(entity, data);
    }

    @Override
    public void removeEnchantment(
            @NotNull Entity entity,
            @NotNull CustomEnchantment customEnchantment
    ) {
        customEnchantment.removeFrom(entity);
    }

    private void removeEnchantment(@NotNull Entity entity, @NotNull Key key) {
        this.enchantmentRegistry.get(key).ifPresent(enchantment -> enchantment.removeFrom(entity));
    }

    @Override
    public @NotNull Collection<CustomEnchantment> enchantments(@NotNull Entity holder) {
        Map<Key, EnchantmentData> dataMap = this.enchantmentCache.get(holder);
        if (dataMap == null) {
            loadData(holder);
            dataMap = this.enchantmentCache.get(holder);
        }
        Collection<CustomEnchantment> enchantments = new HashSet<>(dataMap.size());
        for (Key key : dataMap.keySet()) {
            this.enchantmentRegistry.get(key).ifPresent(enchantments::add);
        }
        return enchantments;
    }

    @Override
    public @NotNull Optional<@NotNull EnchantmentData> enchantmentData(
            @NotNull Entity holder,
            @NotNull CustomEnchantment enchantment
    ) {
        if (!this.enchantmentCache.containsKey(holder)) {
            loadData(holder);
        }
        return Optional.ofNullable(this.enchantmentCache.get(holder))
                .flatMap(map -> Optional.ofNullable(map.get(enchantment.key())));
    }

    @Override
    public boolean hasEnchantment(@NotNull Entity holder, @NotNull CustomEnchantment enchantment) {
        return hasEnchantment(holder, enchantment.key());
    }

    @Override
    public boolean hasEnchantment(@NotNull Entity holder, @NotNull Key key) {
        return this.enchantmentCache.getOrDefault(holder, Collections.emptyMap()).containsKey(key);
    }

}
