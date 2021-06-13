package com.github.md5sha256.addictiveexperience.implementation.plant;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.google.inject.Inject;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PDCResolver implements PlantDataResolver {

    private final NamespacedKey key;

    @Inject
    public PDCResolver(@NotNull Plugin plugin) {
        this.key = new NamespacedKey(plugin, "drug-plant-data");
    }

    @Override
    public @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull final ChunkPosition chunk) {
        final PersistentDataContainer container = chunk.getChunk().getPersistentDataContainer();
        final byte[] raw = container.get(this.key, PersistentDataType.BYTE_ARRAY);
        if (raw == null) {
            return new HashMap<>();
        }
        final String string = new String(raw, StandardCharsets.UTF_8);
        final ConfigurationLoader<?> loader = loader(string);
        final Collection<DrugPlantData> data;
        try {
            final ConfigurationNode root = loader.load();
            data = root.getList(DrugPlantData.class, Collections.emptyList());
        } catch (IOException ex) {
            // FIXME log warning
            ex.printStackTrace();
            return new HashMap<>();
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

    private ConfigurationLoader<?> loader(@NotNull String raw) {
        final StringReader reader = new StringReader(raw);
        return GsonConfigurationLoader.builder()
                                      .source(() -> new BufferedReader(reader))
                                      .lenient(false)
                                      .build();
    }

    private ConfigurationLoader<?> loader(@NotNull OutputStream os) {
        final OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        return GsonConfigurationLoader.builder()
                                      .sink(() -> new BufferedWriter(osw))
                                      .lenient(false)
                                      .build();
    }
}
