package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.fixtures.DummyDrugForm;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
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
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TestPlantHandler {

    private static DrugPlantData plantData;
    private static ServerMock mock;
    private static Plugin plugin;
    private static WorldMock world;
    private static PlantDataResolver resolver;

    @BeforeAll
    public static void init() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin("plugin");
        DrugRegistry drugRegistry = new SimpleDrugRegistry(plugin);
        resolver = new PDCResolver(plugin, drugRegistry);
        IDrug drug = new DummyDrugImpl(Bukkit.getItemFactory(), Key.key("dummy:dummy"), "dummy", Material.COCOA_BEANS, "", new DummyDrugForm(Bukkit.getItemFactory()));
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
        mock = null;
        plugin = null;
        world = null;
        plantData = null;
        resolver = null;
        MockBukkit.unmock();
    }

    @Test
    public void testSerialization() {
        BukkitScheduler scheduler = mock.getScheduler();
        PlantHandlerImpl plantHandler = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler.shutdown();
        BlockPosition position = new BlockPosition(world, 0, 0, 0);
        plantHandler.addEntry(position, plantData);
        Assertions.assertSame(plantData, plantHandler.plantData(position).orElse(null));
        plantHandler.saveData();
        plantHandler = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler.shutdown();
        plantHandler.loadData(new ChunkPosition(position.getChunk()));
        DrugPlantData deserialized = plantHandler.plantData(position).orElse(null);
        Assertions.assertNotNull(deserialized);

        plantData.elapsed().stop();
        deserialized.elapsed().setElapsedTime(plantData.growthTimeElapsedMillis(), TimeUnit.MILLISECONDS);
        Assertions.assertTrue(plantData.isSimilar(deserialized));
    }

    @Test
    public void testGettersAndSetters() {
        final BukkitScheduler scheduler = mock.getScheduler();
        final PlantHandlerImpl plantHandler = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler.shutdown();
        final BlockPosition blockPosition = new BlockPosition(world, 0, 0, 0);
        plantHandler.addEntry(blockPosition, plantData);
        final Optional<DrugPlantData> optional = plantHandler.plantData(blockPosition);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertSame(plantData, optional.get());
        plantHandler.removeEntry(blockPosition);
        Assertions.assertTrue(plantHandler.plantData(blockPosition).isEmpty());
    }

    @Test
    public void testUpdating() {
        final BukkitScheduler scheduler = mock.getScheduler();
        final PlantHandlerImpl plantHandler = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler.shutdown();
        plantData.elapsed().stop();
        plantData.elapsed().setElapsedTime(0, TimeUnit.MILLISECONDS);
        final DrugPlantData copy = plantData.toBuilder().build();
        final BlockPosition position1 = new BlockPosition(world, 0, 0, 0);
        final BlockPosition position2 = new BlockPosition(world, 1, 1, 1);
        copy.elapsed().stop();
        copy.elapsed().setElapsedTime(copy.meta().growthTimeMillis(), TimeUnit.MILLISECONDS);
        plantHandler.addEntry(position1, plantData);
        plantHandler.addEntry(position2, copy);
        plantHandler.update();
        Assertions.assertTrue(plantHandler.plantData(position1).isPresent());
        Assertions.assertTrue(plantHandler.plantData(position2).isEmpty());
        plantHandler.addEntry(position2, copy);
        plantHandler.updatePosition(position2);
        Assertions.assertTrue(plantHandler.plantData(position2).isEmpty());
    }

    @Test
    public void testListeners() {
        BukkitScheduler scheduler = mock.getScheduler();
        PlantHandlerImpl plantHandler1 = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler1.shutdown();
        BlockPosition position = new BlockPosition(world, 0, 0, 0);
        plantHandler1.addEntry(position, plantData);
        plantHandler1.saveData();
        PlantHandlerImpl plantHandler2 = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler2.shutdown();
        Chunk chunk = position.getChunk();
        plantHandler2.onChunkLoad( new ChunkLoadEvent(chunk, false));
        final Optional<DrugPlantData> optional = plantHandler2.plantData(position);
        Assertions.assertTrue(optional.isPresent());
        final DrugPlantData deserialized = optional.get();
        plantData.elapsed().stop();
        deserialized.elapsed().stop();
        deserialized.elapsed().setElapsedTime(plantData.growthTimeElapsedMillis(), TimeUnit.MILLISECONDS);
        Assertions.assertTrue(plantData.isSimilar(optional.get()));
        plantHandler2.removeEntry(position);
        plantHandler2.onChunkUnload(new ChunkUnloadEvent(chunk));
        PlantHandlerImpl plantHandler3 = new PlantHandlerImpl(scheduler, plugin, resolver);
        plantHandler3.shutdown();
        Assertions.assertTrue(plantHandler3.plantData(position).isEmpty());
    }

}
