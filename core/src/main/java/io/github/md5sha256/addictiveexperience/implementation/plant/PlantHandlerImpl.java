package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import com.github.md5sha256.spigotutils.Common;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.World;
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
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
public final class PlantHandlerImpl implements IPlantHandler {

    private final PlantDataResolver resolver;
    private final Map<ChunkPosition, Map<Long, DrugPlantData>> cache = new ConcurrentHashMap<>();

    private final BukkitTask task;

    @Inject
    public PlantHandlerImpl(@NotNull BukkitScheduler scheduler,
                            @NotNull Plugin plugin,
                            @NotNull PlantDataResolver resolver) {
        long ticks = Common.toTicks(5, TimeUnit.SECONDS);
        this.task = scheduler.runTaskTimer(plugin, (Runnable) this::saveData, ticks, ticks);
        this.resolver = resolver;
    }

    @Override
    public void addEntry(@NotNull BlockPosition position, @NotNull DrugPlantData data) {
        this.cache.computeIfAbsent(new ChunkPosition(position.getChunk()), x -> new HashMap<>())
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
            Collection<DrugPlantData> copy = entry.getValue().values().stream()
                    .<DrugPlantData>map(data -> data.toBuilder().build())
                    .toList();
            this.resolver.saveDataAsync(entry.getKey(), copy);
        }
    }


    private void saveDataSync() {
        for (Map.Entry<ChunkPosition, Map<Long, DrugPlantData>> entry : this.cache.entrySet()) {
            this.resolver.saveData(entry.getKey(), entry.getValue().values());
        }
    }


    public void saveDataSync(@NotNull ChunkPosition chunk) {
        final Collection<DrugPlantData> data = this.cache
                .getOrDefault(chunk, Collections.emptyMap())
                .values();
        this.resolver.saveDataAsync(chunk, data);
    }

    @Override
    public void saveData(@NotNull ChunkPosition chunk) {
        Collection<DrugPlantData> copy = this.cache
                .getOrDefault(chunk, Collections.emptyMap())
                .values()
                .stream()
                .<DrugPlantData>map(data -> data.toBuilder().build())
                .toList();
        this.resolver.saveDataAsync(chunk, copy);
    }


    private void loadDataSync(@NotNull ChunkPosition chunk) {
        final Map<Long, DrugPlantData> data = this.resolver.loadData(chunk);
        if (data.isEmpty()) {
            return;
        }
        for (DrugPlantData plantData : data.values()) {
            plantData.elapsed().start();
        }
        // Don't overwrite cached data
        this.cache.putIfAbsent(chunk, data);
    }

    @Override
    public void loadData(@NotNull ChunkPosition chunk) {
        this.resolver.loadDataAsync(chunk).thenAccept(data -> {
            if (data.isEmpty()) {
                return;
            }
            for (DrugPlantData plantData : data.values()) {
                plantData.elapsed().start();
            }
            // Don't overwrite cached data
            this.cache.putIfAbsent(chunk, data);
        });
    }

    public void shutdown() {
        if (!this.task.isCancelled()) {
            this.task.cancel();
            saveDataSync();
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
        final PlantDataResolver resolver = this.resolver;
        final Map<Long, DrugPlantData> data = this.cache.get(position);
        if (data != null) {
            for (DrugPlantData plantData : data.values()) {
                // Pause the plant data
                plantData.elapsed().stop();
            }
            resolver.saveDataAsync(position, data.values());
        } else {
            resolver.clearDataAsync(position);
        }
    }

}
