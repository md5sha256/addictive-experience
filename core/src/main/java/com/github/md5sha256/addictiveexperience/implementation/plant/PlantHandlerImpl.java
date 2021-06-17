package com.github.md5sha256.addictiveexperience.implementation.plant;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import com.github.md5sha256.spigotutils.Common;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public final class PlantHandlerImpl implements IPlantHandler {

    private final PlantDataResolver resolver;

    private final Map<ChunkPosition, Map<Long, DrugPlantData>> cache = new HashMap<>();

    private final BukkitTask task;

    @Inject
    public PlantHandlerImpl(@NotNull BukkitScheduler scheduler,
                            @NotNull Plugin plugin,
                            @NotNull PlantDataResolver dataResolver) {
        this.resolver = dataResolver;
        long ticks = Common.toTicks(5, TimeUnit.SECONDS);
        this.task = scheduler.runTaskTimer(plugin, (Runnable) this::saveData, ticks, ticks);
    }

    @Override
    public void addEntry(@NotNull BlockPosition position, @NotNull DrugPlantData data) {
        this.cache.computeIfAbsent(new ChunkPosition(position.getChunk()),
                                   (unused) -> new HashMap<>())
                  .put(position.getPosition(), data);
    }

    @Override
    public void removeEntry(@NotNull BlockPosition position) {
        final ChunkPosition chunkPosition = new ChunkPosition(position.getChunk());
        final Map<Long, DrugPlantData> map = this.cache.get(chunkPosition);
        if (map != null) {
            map.remove(position.getPosition());
        }
    }

    @Override
    public void update() {
        final Map<ChunkPosition, Collection<Long>> toRemove = new HashMap<>();
        for (Map.Entry<ChunkPosition, Map<Long, DrugPlantData>> entry : this.cache.entrySet()) {
            List<Long> positions = new ArrayList<>(entry.getValue().size() / 2);
            for (Map.Entry<Long, DrugPlantData> data : entry.getValue().entrySet()) {
                final DrugPlantData plantData = data.getValue();
                if (plantData.remainingMillis() == 0) {
                    positions.add(data.getKey());
                }
            }
            toRemove.put(entry.getKey(), positions);
        }
        for (Map.Entry<ChunkPosition, Collection<Long>> entry : toRemove.entrySet()) {
            final Map<Long, DrugPlantData> map = this.cache.get(entry.getKey());
            // HashMap#keySet#remove is faster
            entry.getValue().forEach(map.keySet()::remove);
        }
        // Remove empty values
        this.cache.values().removeIf(Map::isEmpty);
    }

    @Override
    public @NotNull Optional<@NotNull DrugPlantData> updatePosition(@NotNull BlockPosition position) {
        final ChunkPosition chunkPosition = new ChunkPosition(position.getChunk());
        final Map<Long, DrugPlantData> data = this.cache.get(chunkPosition);
        if (data == null) {
            return Optional.empty();
        }
        final long compressedPosition = position.getPosition();
        final DrugPlantData plantData = data.get(compressedPosition);
        if (plantData == null) {
            return Optional.empty();
        }
        if (plantData.remainingMillis() == 0) {
            data.remove(compressedPosition);
            if (data.isEmpty()) {
                this.cache.remove(chunkPosition);
                return Optional.empty();
            }
            return Optional.empty();
        }
        return Optional.of(plantData);
    }

    @Override
    public @NotNull Optional<@NotNull DrugPlantData> plantData(@NotNull BlockPosition position) {
        final ChunkPosition chunkPosition = new ChunkPosition(position.getChunk());
        final Map<Long, DrugPlantData> data = this.cache.get(chunkPosition);
        if (data == null) {
            return Optional.empty();
        }
        final long compressedPosition = position.getPosition();
        return Optional.ofNullable(data.get(compressedPosition));
    }

    @Override
    public void saveData() {
        for (Map.Entry<ChunkPosition, Map<Long, DrugPlantData>> entry : this.cache.entrySet()) {
            this.resolver.saveData(entry.getKey(), entry.getValue().values());
        }
    }

    @Override
    public void loadData(@NotNull ChunkPosition chunk) {
        final Map<Long, DrugPlantData> data = this.resolver.loadData(chunk);
        for (DrugPlantData plantData : data.values()) {
            plantData.elapsed().start();
        }
        this.cache.put(chunk, data);
    }

    @Override
    public void saveData(@NotNull ChunkPosition chunk) {
        final Collection<DrugPlantData> data = this.cache
                .getOrDefault(chunk, Collections.emptyMap())
                .values();
        this.resolver.saveData(chunk, data);
    }

    public void shutdown() {
        if (!this.task.isCancelled()) {
            this.task.cancel();
            unregisterEvents();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(@NotNull ChunkLoadEvent event) {
        loadData(new ChunkPosition(event.getChunk()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(@NotNull ChunkUnloadEvent event) {
        final ChunkPosition position = new ChunkPosition(event.getChunk());
        final Map<Long, DrugPlantData> data = this.cache.get(position);
        final Collection<DrugPlantData> toSave;
        if (data != null) {
            for (DrugPlantData plantData : data.values()) {
                // Pause the plant data
                plantData.elapsed().stop();
            }
            toSave = data.values();
        } else {
            toSave = Collections.emptySet();
        }
        this.resolver.saveData(position, toSave);
    }

}
