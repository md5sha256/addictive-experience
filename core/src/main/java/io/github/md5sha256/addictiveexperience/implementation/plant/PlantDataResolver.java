package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PlantDataResolver {

    @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull ChunkPosition chunk);

    default @NotNull CompletableFuture<@NotNull Map<Long, @NotNull DrugPlantData>> loadDataAsync(@NotNull ChunkPosition chunk) {
        return CompletableFuture.completedFuture(loadData(chunk));
    }

    void saveData(@NotNull ChunkPosition chunk, @NotNull Collection<DrugPlantData> data);

    default @NotNull CompletableFuture<Void> saveDataAsync(@NotNull ChunkPosition chunk,
                                                           @NotNull Collection<DrugPlantData> data) {
        saveData(chunk, data);
        return CompletableFuture.completedFuture(null);
    }

    void clearData(@NotNull ChunkPosition chunk);

    default @NotNull CompletableFuture<Void> clearDataAsync(@NotNull ChunkPosition chunk) {
        clearData(chunk);
        return CompletableFuture.completedFuture(null);
    }

}
