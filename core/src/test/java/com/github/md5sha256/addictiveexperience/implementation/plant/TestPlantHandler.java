package com.github.md5sha256.addictiveexperience.implementation.plant;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import com.github.md5sha256.addictiveexperience.fixtures.DummyDrugImpl;
import com.github.md5sha256.addictiveexperience.implementation.drugs.SimpleDrugRegistry;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestPlantHandler {

    private static DrugPlantData plantData;
    private static ServerMock mock;
    private static Plugin plugin;
    private static WorldMock world;
    private static PlantDataResolverFactory factory;

    @BeforeAll
    public static void init() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin("plugin");
        DrugRegistry drugRegistry = new SimpleDrugRegistry(plugin);
        factory = new SimplePlantDataResolverFactory(plugin, drugRegistry);
        IDrug drug = new DummyDrugImpl(Bukkit.getItemFactory(), Key.key("dummy:dummy"), "dummy", Material.COCOA, "");
        drugRegistry.registerComponent(drug);
        world = new WorldMock();
        mock.addWorld(world);
        plantData = DrugPlantData.builder()
                .elapsed(Stopwatches.variableStopwatch(GuavaAdapter.ofUnstarted()))
                .startTimeEpochMilli(System.currentTimeMillis())
                .position(new BlockPosition(world, 0, 0, 0))
                .meta(DrugPlantMeta.defaultMeta(drug))
                .build();
    }

    @AfterAll
    public static void teardown() {
        plantData = null;
        factory = null;
        MockBukkit.unmock();
    }

    @Test
    public void testSerialization() {
        BukkitScheduler scheduler = mock.getScheduler();
        PlantHandlerImpl plantHandler = new PlantHandlerImpl(scheduler, plugin, factory);
        plantHandler.shutdown();
        BlockPosition position = new BlockPosition(world, 0, 0, 0);
        plantHandler.addEntry(position, plantData);
        Assertions.assertSame(plantData, plantHandler.plantData(position).orElse(null));
        plantHandler.saveData();
        plantHandler = new PlantHandlerImpl(scheduler, plugin, factory);
        plantHandler.shutdown();
        plantHandler.loadData(new ChunkPosition(position.getChunk()));
        DrugPlantData deserialized = plantHandler.plantData(position).orElse(null);
        Assertions.assertNotNull(deserialized);

        plantData.elapsed().stop();
        deserialized.elapsed().setElapsedTime(plantData.growthTimeElapsedMillis(), TimeUnit.MILLISECONDS);
        Assertions.assertTrue(plantData.isSimilar(deserialized));
    }

}
