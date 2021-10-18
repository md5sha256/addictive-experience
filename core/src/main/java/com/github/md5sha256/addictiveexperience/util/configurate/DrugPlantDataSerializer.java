package com.github.md5sha256.addictiveexperience.util.configurate;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantDataBuilder;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class DrugPlantDataSerializer implements TypeSerializer<DrugPlantData> {

    private static final String KEY_META = "meta";
    private static final String KEY_START_TIME = "start-time";
    private static final String KEY_STOPWATCH = "stopwatch";
    private static final String KEY_POSITION = "position";

    private final World world;

    public DrugPlantDataSerializer(@NotNull World world) {
        this.world = world;
    }

    @Override
    public DrugPlantData deserialize(final Type type,
                                     final ConfigurationNode node) throws SerializationException {

        final ConfigurationNode meta = node.node(KEY_META);
        final ConfigurationNode stopwatch = node.node(KEY_STOPWATCH);
        final ConfigurationNode startTime = node.node(KEY_START_TIME);
        final ConfigurationNode position = node.node(KEY_POSITION);
        final DrugPlantDataBuilder builder = DrugPlantData.builder();
        final DrugPlantMeta plantMeta = meta.get(DrugPlantMeta.class);
        final VariableStopwatch elapsed = stopwatch.get(VariableStopwatch.class);
        if (plantMeta == null) {
            throw new SerializationException("Missing drug plant meta!");
        }
        if (elapsed == null) {
            throw new SerializationException("Missing elapsed time!");
        }
        builder.meta(plantMeta)
                .position(new BlockPosition(world, position.getLong()))
               .startTimeEpochMilli(startTime.getLong())
               .elapsed(elapsed);
        try {
            return builder.build();
        } catch (IllegalArgumentException ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public void serialize(final Type type,
                          @Nullable final DrugPlantData obj,
                          final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.removeChild(KEY_META);
            node.removeChild(KEY_STOPWATCH);
            node.removeChild(KEY_START_TIME);
            node.removeChild(KEY_POSITION);
            return;
        }
        final ConfigurationNode meta = node.node(KEY_META);
        meta.set(obj.meta());
        final ConfigurationNode stopwatch = node.node(KEY_STOPWATCH);
        stopwatch.set(obj.elapsed());
        final ConfigurationNode startTime = node.node(KEY_START_TIME);
        startTime.set(obj.startTimeEpochMillis());
        final ConfigurationNode position = node.node(KEY_POSITION);
        position.set(obj.position().getPosition());
    }
}
