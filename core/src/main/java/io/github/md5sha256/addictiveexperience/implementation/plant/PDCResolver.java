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
    private final ConfigurationOptions options;

    @AssistedInject
    PDCResolver(@NotNull Plugin plugin, @NotNull DrugRegistry drugRegistry,
                @Assisted @NotNull World world) {
        this.key = new NamespacedKey(plugin, "drug-plant-data");
        this.options = ConfigurationOptions
                .defaults()
                .serializers(builder -> builder
                        .register(DrugPlantMeta.class, new DrugPlantMetaSerializer(drugRegistry))
                        .register(DrugPlantData.class, new DrugPlantDataSerializer(world))
                        .register(Key.class, new AdventureKeySerializer())
                        .register(VariableStopwatch.class, new VariableStopwatchSerializer()));
    }

    @Override
    public @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull final ChunkPosition chunk) {
        final PersistentDataContainer container = chunk.getChunk().getPersistentDataContainer();
        final byte[] raw = container.get(this.key, PersistentDataType.BYTE_ARRAY);
        if (raw == null) {
            return Collections.emptyMap();
        }
        final ConfigurationLoader<?> loader = loader(raw);
        final Collection<DrugPlantData> data;
        try {
            final ConfigurationNode root = loader.load();
            data = root.getList(DrugPlantData.class, Collections.emptyList());
        } catch (IOException ex) {
            // FIXME log warning
            ex.printStackTrace();
            return Collections.emptyMap();
        }
        final Map<Long, DrugPlantData> map = new HashMap<>(data.size());
        for (DrugPlantData plantData : data) {
            map.put(plantData.position().getPosition(), plantData);
        }
        return new HashMap<>(map);
    }

    @Override
    public void saveData(@NotNull final ChunkPosition chunk,
                         @NotNull final Collection<DrugPlantData> data) {
        if (data.isEmpty()) {
            clearData(chunk);
            return;
        }
        final PersistentDataContainer container = chunk.getChunk().getPersistentDataContainer();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        final ConfigurationLoader<?> loader = loader(bos);
        final ConfigurationNode node = loader.createNode();
        final List<DrugPlantData> dataAsList;
        if (data instanceof List) {
            dataAsList = (List<DrugPlantData>) data;
        } else {
            dataAsList = new ArrayList<>(data);
        }
        try {
            node.setList(DrugPlantData.class, dataAsList);
            loader.save(node);
        } catch (IOException ex) {
            // FIXME log warning
            ex.printStackTrace();
            // Remove all data
            container.remove(this.key);
            return;
        }
        container.set(this.key, PersistentDataType.BYTE_ARRAY, bos.toByteArray());
    }

    @Override
    public void clearData(@NotNull final ChunkPosition chunk) {
        chunk.getChunk().getPersistentDataContainer().remove(this.key);
    }

    private ConfigurationLoader<?> loader(byte[] raw) {
        final Reader reader = new InputStreamReader(new ByteArrayInputStream(raw), StandardCharsets.UTF_8);
        return GsonConfigurationLoader.builder()
                                      .defaultOptions(this.options)
                                      .source(() -> new BufferedReader(reader))
                                      .lenient(true)
                                      .build();
    }

    private ConfigurationLoader<?> loader(@NotNull OutputStream os) {
        final OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        return GsonConfigurationLoader.builder()
                                      .defaultOptions(this.options)
                                      .sink(() -> new BufferedWriter(osw))
                                      .lenient(true)
                                      .build();
    }
}
