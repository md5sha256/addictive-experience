package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public interface PlantDataResolver {

    @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull ChunkPosition chunk);

    void saveData(@NotNull ChunkPosition chunk, @NotNull Collection<DrugPlantData> data);

    void clearData(@NotNull ChunkPosition chunk);

}
