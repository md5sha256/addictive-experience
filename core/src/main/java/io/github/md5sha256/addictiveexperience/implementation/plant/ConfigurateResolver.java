package io.github.md5sha256.addictiveexperience.implementation.plant;

import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.util.configurate.AdventureKeySerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.BlockPositionSerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.DrugPlantDataSerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.DrugPlantMetaSerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.VariableStopwatchSerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.WorldSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

public final class ConfigurateResolver {

    private final ConfigurationOptions options;

    public ConfigurateResolver(@NotNull Plugin plugin, @NotNull DrugRegistry drugRegistry) {
        this.options = ConfigurationOptions
                .defaults()
                .serializers(builder -> builder
                        .register(World.class, new WorldSerializer(plugin.getServer()))
                        .register(BlockPosition.class, new BlockPositionSerializer())
                        .register(DrugPlantMeta.class, new DrugPlantMetaSerializer(drugRegistry))
                        .register(DrugPlantData.class, new DrugPlantDataSerializer())
                        .register(Key.class, new AdventureKeySerializer())
                        .register(VariableStopwatch.class, new VariableStopwatchSerializer()));
    }

    public Map<Long, @NotNull DrugPlantData> fromChunkBytes(byte[] bytes) {
        if (bytes == null) {
            return Collections.emptyMap();
        }
        final ConfigurationLoader<?> loader = loader(bytes);
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

    public DrugPlantData fromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final ConfigurationLoader<?> loader = loader(bytes);

        try {
            final ConfigurationNode root = loader.load();
            return root.get(DrugPlantData.class);
        } catch (IOException ex) {
            // FIXME log warning
            ex.printStackTrace();
            return null;
        }
    }

    public byte @Nullable [] toChunkBytes(@NotNull Collection<DrugPlantData> data) {

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
            return null;
        }
        return bos.toByteArray();
    }

    public byte @Nullable [] toBytes(@NotNull DrugPlantData data) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        final ConfigurationLoader<?> loader = loader(bos);
        final ConfigurationNode node = loader.createNode();
        final List<DrugPlantData> dataAsList;
        try {
            node.set(DrugPlantData.class, data);
            loader.save(node);
        } catch (IOException ex) {
            // FIXME log warning
            ex.printStackTrace();
            // Remove all data
            return null;
        }
        return bos.toByteArray();
    }

    private ConfigurationLoader<?> loader(byte[] raw) {
        final Reader reader = new InputStreamReader(new ByteArrayInputStream(raw),
                StandardCharsets.UTF_8);
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
