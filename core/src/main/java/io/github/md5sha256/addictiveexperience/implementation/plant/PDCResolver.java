package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

final class PDCResolver implements PlantDataResolver {

    private final NamespacedKey key;
    private final ConfigurateResolver resolver;

    @AssistedInject
    PDCResolver(@NotNull Plugin plugin, @NotNull DrugRegistry drugRegistry) {
        this.key = new NamespacedKey(plugin, "drug-plant-data");
        this.resolver = new ConfigurateResolver(plugin, drugRegistry);
    }

    @Override
    public @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull final ChunkPosition chunk) {
        final PersistentDataContainer container = chunk.getChunk().getPersistentDataContainer();
        final byte[] raw = container.get(this.key, PersistentDataType.BYTE_ARRAY);
        return this.resolver.fromChunkBytes(raw);
    }

    @Override
    public void saveData(@NotNull final ChunkPosition chunk,
                         @NotNull final Collection<DrugPlantData> data) {
        if (data.isEmpty()) {
            clearData(chunk);
            return;
        }
        final byte[] bytes = this.resolver.toChunkBytes(data);
        if (bytes == null) {
            clearData(chunk);
            return;
        }
        final PersistentDataContainer container = chunk.getChunk().getPersistentDataContainer();
        container.set(this.key, PersistentDataType.BYTE_ARRAY, bytes);
    }

    @Override
    public void clearData(@NotNull final ChunkPosition chunk) {
        chunk.getChunk().getPersistentDataContainer().remove(this.key);
    }
}
