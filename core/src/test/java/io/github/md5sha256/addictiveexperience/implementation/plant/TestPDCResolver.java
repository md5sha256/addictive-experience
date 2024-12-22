package io.github.md5sha256.addictiveexperience.implementation.plant;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.world.WorldMock;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.fixtures.DummyDrugImpl;
import io.github.md5sha256.addictiveexperience.implementation.drugs.SimpleDrugRegistry;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TestPDCResolver {

    @BeforeAll
    public static void init() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    public void testPDCResolver() {
        Plugin plugin = MockBukkit.createMockPlugin("plugin");
        DrugRegistry drugRegistry = new SimpleDrugRegistry(plugin);
        IDrug drug = new DummyDrugImpl(Bukkit.getItemFactory(), Key.key("dummy:dummy"), "dummy", Material.COCOA, "");
        drugRegistry.registerComponent(drug);
        World world = new WorldMock();
        Chunk chunk = world.getChunkAt(0, 0);
        PlantDataResolver resolver = new PDCResolver(plugin, drugRegistry, world);
        ChunkPosition chunkPosition = new ChunkPosition(chunk);
        DrugPlantData data = DrugPlantData.builder()
                .elapsed(Stopwatches.variableStopwatch(GuavaAdapter.ofUnstarted()))
                .startTimeEpochMilli(System.currentTimeMillis())
                .position(new BlockPosition(world, 0, 0, 0))
                .meta(DrugPlantMeta.defaultMeta(drug))
                .build();
        Collection<DrugPlantData> collection = Collections.singleton(data);
        resolver.saveData(chunkPosition, collection);
        Collection<DrugPlantData> loaded = resolver.loadData(chunkPosition).values();
        DrugPlantData deserialized = loaded.iterator().next();
        data.elapsed().stop();
        deserialized.elapsed().stop();
        deserialized.elapsed().setElapsedTime(data.growthTimeElapsedMillis(), TimeUnit.MILLISECONDS);
        Assertions.assertTrue(data.isSimilar(deserialized));
    }

}
