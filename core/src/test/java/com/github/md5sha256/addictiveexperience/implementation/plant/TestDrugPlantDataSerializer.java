package com.github.md5sha256.addictiveexperience.implementation.plant;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.fixtures.DummyDrugImpl;
import com.github.md5sha256.addictiveexperience.implementation.drugs.SimpleDrugRegistry;
import com.github.md5sha256.addictiveexperience.util.configurate.AdventureKeySerializer;
import com.github.md5sha256.addictiveexperience.util.configurate.DrugPlantDataSerializer;
import com.github.md5sha256.addictiveexperience.util.configurate.DrugPlantMetaSerializer;
import com.github.md5sha256.addictiveexperience.util.configurate.VariableStopwatchSerializer;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.concurrent.TimeUnit;

public class TestDrugPlantDataSerializer {

    private static ServerMock mock;

    @BeforeAll
    public static void init() {
        mock = MockBukkit.mock();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    public void testSerialization() {
        Plugin plugin = MockBukkit.createMockPlugin();
        WorldMock world = new WorldMock();
        mock.addWorld(world);
        IDrug mockDrug = new DummyDrugImpl(
                mock.getItemFactory(),
                Key.key("dummy", "dummy-drug"),
                "dummy drug",
                Material.IRON_AXE,
                "");
        DrugRegistry drugRegistry = new SimpleDrugRegistry(plugin);
        drugRegistry.registerComponent(mockDrug);
        DrugPlantData plantData = DrugPlantData.builder()
                .startTimeEpochMilli(System.currentTimeMillis())
                .position(new BlockPosition(world, 0, 0, 0))
                .elapsed(Stopwatches.newInstance())
                .meta(DrugPlantMeta.defaultMeta(mockDrug))
                .build();
        DrugPlantMetaSerializer serializer = new DrugPlantMetaSerializer(drugRegistry);
        ConfigurationLoader<?> loader = GsonConfigurationLoader.builder()
                .defaultOptions(options -> options.serializers(serializers -> {
                    serializers.register(DrugPlantMeta.class, serializer);
                    serializers.register(DrugPlantData.class, new DrugPlantDataSerializer(world));
                    serializers.register(VariableStopwatch.class, new VariableStopwatchSerializer());
                    serializers.register(Key.class, new AdventureKeySerializer());
                }))
                .build();
        ConfigurationNode node = loader.createNode();
        DrugPlantData deserialized;
        try {
            node.set(plantData);
            deserialized = node.get(DrugPlantData.class);
        } catch (SerializationException ex) {
            Assertions.fail(ex);
            return;
        }
        Assertions.assertNotNull(deserialized);
        plantData.elapsed().stop();
        deserialized.elapsed().stop();
        deserialized.elapsed().setElapsedTime(plantData.growthTimeElapsedMillis(), TimeUnit.MILLISECONDS);
        Assertions.assertTrue(plantData.isSimilar(deserialized));
    }

}
