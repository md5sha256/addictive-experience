package com.github.md5sha256.addictiveexperience.implementation;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.papermc.lib.PaperLib;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePlantDatabase {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;
    private final NamespacedKey containerKey;

    private final Set<Location> chunkUnloadRemoval = ConcurrentHashMap.newKeySet();

    private final Cache<Location, DrugPlantData> dataCache;


    public SimplePlantDatabase(@NotNull Plugin plugin, @NotNull BukkitScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
        this.containerKey = new NamespacedKey(plugin, "drug-plant-data");
        this.dataCache = CacheBuilder.newBuilder().concurrencyLevel(2).<Location, DrugPlantData>removalListener(entry -> {
            if (this.chunkUnloadRemoval.contains(entry.getKey())) {
                return;
            } else {
            }
        }).build();
    }

    private NamespacedKey keyForBlock(@NotNull Location location) {
        return keyForBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    private NamespacedKey keyForBlock(int x, int y, int z) {
        return new NamespacedKey(this.plugin, String.format("%d-%d-%d", x, y, z));
    }

    public void loadData(@NotNull Chunk chunk) {

    }

    public @NotNull Optional<@NotNull DrugPlantData> dataAt(@NotNull Location location) {
        return Optional.empty();
    }

    public @NotNull Optional<@NotNull DrugPlantData> query(@NotNull Location location) {
        final Chunk chunk = location.getChunk();
        if (!chunk.isLoaded()) {
            // FIXME warn unloaded chunk pdc lookup
        }
        final PersistentDataContainer root = chunk.getPersistentDataContainer();
        final PersistentDataContainer parentContainer = root.get(this.containerKey, PersistentDataType.TAG_CONTAINER);
        if (parentContainer == null) {
            return Optional.empty();
        }
        final NamespacedKey key = keyForBlock(location);
        final String raw = root.get(key, PersistentDataType.STRING);
        // FIXME implementation
        return Optional.empty();
    }

    public @NotNull CompletableFuture<?> setData(@NotNull Location location, @NotNull DrugPlantData data) {
        final CompletableFuture<?> future = new CompletableFuture<>();
        final NamespacedKey key = keyForBlock(location);
        PaperLib.getChunkAtAsync(location).thenAccept(chunk -> this.scheduler.callSyncMethod(this.plugin, () -> {
            writeData(chunk.getPersistentDataContainer(), key, data);
            future.complete(null);
            return null;
        }));
        return future;
    }

    private void writeData(@NotNull PersistentDataContainer root, @NotNull NamespacedKey key, @NotNull DrugPlantData data) {
        final PersistentDataAdapterContext adapterContext = root.getAdapterContext();
        final PersistentDataContainer parentContainer =
            root.getOrDefault(this.containerKey, PersistentDataType.TAG_CONTAINER, adapterContext.newPersistentDataContainer());
        final String raw;
        // FIXME implementation
    }


}
