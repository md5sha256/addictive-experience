package io.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.spigotutils.DeregisterableListener;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IPlantHandler extends DeregisterableListener {

    void addEntry(@NotNull BlockPosition position, @NotNull DrugPlantData data);

    void removeEntry(@NotNull BlockPosition position);

    void update();

    @NotNull Optional<@NotNull DrugPlantData> updatePosition(@NotNull BlockPosition position);

    @NotNull Optional<@NotNull DrugPlantData> plantData(@NotNull BlockPosition position);

    void saveData();

    void saveData(@NotNull ChunkPosition chunk);

    void loadData(@NotNull ChunkPosition chunk);

    void shutdown();

}
