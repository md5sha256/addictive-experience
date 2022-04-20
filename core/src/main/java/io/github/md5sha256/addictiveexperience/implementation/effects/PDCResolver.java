package io.github.md5sha256.addictiveexperience.implementation.effects;

import io.github.md5sha256.addictiveexperience.api.effect.EffectData;
import io.github.md5sha256.addictiveexperience.util.configurate.AdventureKeySerializer;
import io.github.md5sha256.addictiveexperience.util.configurate.VariableStopwatchSerializer;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import javax.inject.Inject;
import javax.inject.Singleton;
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
import java.util.Collections;
import java.util.Map;

@Singleton
final class PDCResolver implements CustomEffectResolver {

    private static final TypeToken<Map<Key, EffectData>> dataType = new TypeToken<>() {};

    private final ConfigurationOptions options;
    private final NamespacedKey keyData;

    @Inject
    PDCResolver(@NotNull Plugin plugin) {
        this.keyData = new NamespacedKey(plugin, "custom-enchantments");
        this.options = ConfigurationOptions.defaults().serializers(serializers ->
                serializers.register(Key.class, new AdventureKeySerializer())
                        .register(VariableStopwatch.class, new VariableStopwatchSerializer()));
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

    private @NotNull Map<Key, EffectData> loadData(byte[] bytes) {
        final ConfigurationLoader<?> loader = loader(bytes);
        final Map<Key, EffectData> map;
        try {
            final ConfigurationNode node = loader.load();
            map = node.get(dataType);
        } catch (IOException ex) {
            ex.printStackTrace();
            // FIXME log warning
            return Collections.emptyMap();
        }
        if (map == null || map.isEmpty()) {
            return Collections.emptyMap();
        }
        return map;
    }

    @Override
    public @NotNull Map<Key, EffectData> loadData(@NotNull PersistentDataHolder holder) {
        final PersistentDataContainer root = holder.getPersistentDataContainer();
        final byte[] bytes = root.getOrDefault(this.keyData, PersistentDataType.BYTE_ARRAY, new byte[0]);
        if (bytes.length == 0) {
            return Collections.emptyMap();
        }
        return loadData(bytes);
    }

    @Override
    public void clearData(@NotNull PersistentDataHolder holder) {
        holder.getPersistentDataContainer().remove(this.keyData);
    }

    @Override
    public void saveData(@NotNull PersistentDataHolder holder,
                         @NotNull Map<Key, EffectData> dataMap) {
        if (dataMap.isEmpty()) {
            clearData(holder);
            return;
        }
        final PersistentDataContainer root = holder.getPersistentDataContainer();
        final byte[] data = serializeData(dataMap);
        root.set(this.keyData, PersistentDataType.BYTE_ARRAY, data);

    }

    private byte[] serializeData(@NotNull Map<Key, EffectData> dataMap) {
        if (dataMap.isEmpty()) {
            return new byte[0];
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        final ConfigurationLoader<?> loader = loader(bos);
        final ConfigurationNode rootNode = loader.createNode();
        try {
            rootNode.set(dataMap);
            loader.save(rootNode);
        } catch (IOException ex) {
            ex.printStackTrace();
            // FIXME log warning
            return new byte[0];
        }
        return bos.toByteArray();
    }

}
