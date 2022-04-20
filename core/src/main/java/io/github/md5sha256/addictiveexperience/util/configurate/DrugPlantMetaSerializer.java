package io.github.md5sha256.addictiveexperience.util.configurate;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantMetaBuilder;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class DrugPlantMetaSerializer implements TypeSerializer<DrugPlantMeta> {

    private static final String KEY_GROWTH_TIME_MILLIS = "growth-time";
    private static final String KEY_SEED_DROP_PROB = "seed-drop-probability";
    private static final String KEY_SEED_DROP_AMT = "seed-drop-amount";
    private static final String KEY_SEED = "seed";
    private static final String KEY_HARVEST_PROB = "harvest-probability";
    private static final String KEY_HARVEST_AMT = "harvest-amount";
    private static final String KEY_RESULT = "result";

    private final DrugRegistry registry;

    @Inject
    public DrugPlantMetaSerializer(@NotNull DrugRegistry registry) {
        this.registry = registry;
    }

    @Override
    public DrugPlantMeta deserialize(@NotNull Type type, @NotNull ConfigurationNode node)
            throws SerializationException {
        final ConfigurationNode growthTime = node.node(KEY_GROWTH_TIME_MILLIS);
        final ConfigurationNode seed = node.node(KEY_SEED);
        final ConfigurationNode seedDropAmount = node.node(KEY_SEED_DROP_AMT);
        final ConfigurationNode seedDropProbability = node.node(KEY_SEED_DROP_PROB);
        final ConfigurationNode harvestProbability = node.node(KEY_HARVEST_PROB);
        final ConfigurationNode harvestAmount = node.node(KEY_HARVEST_AMT);
        final ConfigurationNode result = node.node(KEY_RESULT);
        final DrugPlantMetaBuilder builder = DrugPlantMeta.builder();
        final Key keySeed = seed.get(Key.class);
        final Key keyResult = result.get(Key.class);
        final IDrugComponent seedComponent;
        final IDrug drugResult;
        if (keySeed != null) {
            seedComponent = this.registry.componentByKey(keySeed).orElse(null);
        } else {
            seedComponent = null;
        }
        if (keyResult != null) {
            drugResult = this.registry.drugByKey(keyResult).orElse(null);
        } else {
            drugResult = null;
        }
        if (drugResult == null) {
            throw new SerializationException("Drug is null");
        }
        builder.growthTimeMillis(growthTime.getLong())
                .seed(seedComponent)
                .seedDropAmount(seedDropAmount.getInt())
                .seedDropProbability(seedDropProbability.getDouble())
                .harvestProbability(harvestProbability.getDouble())
                .harvestAmount(harvestAmount.getInt())
                .result(drugResult);
        try {
            return builder.build();
        } catch (IllegalArgumentException ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable DrugPlantMeta meta,
                          ConfigurationNode node)
            throws SerializationException {
        if (meta == null) {
            return;
        }
        final ConfigurationNode growthTime = node.node(KEY_GROWTH_TIME_MILLIS);
        growthTime.set(meta.growthTimeMillis());
        final ConfigurationNode seed = node.node(KEY_SEED);
        seed.set(meta.seed().map(IDrugComponent::key).orElse(null));
        final ConfigurationNode seedDropAmount = node.node(KEY_SEED_DROP_AMT);
        seedDropAmount.set(meta.seedDropAmount());
        final ConfigurationNode seedDropProbability = node.node(KEY_SEED_DROP_PROB);
        seedDropProbability.set(meta.seedDropProbability());
        final ConfigurationNode harvestProbability = node.node(KEY_HARVEST_PROB);
        harvestProbability.set(meta.harvestSuccessProbability());
        final ConfigurationNode harvestAmount = node.node(KEY_HARVEST_AMT);
        harvestAmount.set(meta.harvestAmount());
        final ConfigurationNode drugResult = node.node(KEY_RESULT);
        drugResult.set(meta.result().key());
    }
}
