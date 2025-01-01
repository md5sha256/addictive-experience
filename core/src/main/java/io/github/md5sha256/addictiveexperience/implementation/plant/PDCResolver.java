package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.util.configurate.AdventureKeySerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.DrugPlantDataSerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.DrugPlantMetaSerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.VariableStopwatchSerializer;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class PDCResolver implements PlantDataResolver {

    private final NamespacedKey key;
    private final ConfigurateResolver resolver;

    @AssistedInject
    PDCResolver(@NotNull Plugin plugin, @NotNull DrugRegistry drugRegistry,
                @Assisted @NotNull World world) {
        this.key = new NamespacedKey(plugin, "drug-plant-data");
        this.resolver = new ConfigurateResolver(plugin, drugRegistry);
    }

    @Override
    public @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull final ChunkPosition chunk) {
        final PersistentDataContainer container = chunk.getChunk().getPersistentDataContainer();
        final byte[] raw = container.get(this.key, PersistentDataType.BYTE_ARRAY);
        return this.resolver.fromBytes(raw);
    }

    @Override
    public void saveData(@NotNull final ChunkPosition chunk,
                         @NotNull final Collection<DrugPlantData> data) {
        if (data.isEmpty()) {
            clearData(chunk);
            return;
        }
        final byte[] bytes = this.resolver.toBytes(data);
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
