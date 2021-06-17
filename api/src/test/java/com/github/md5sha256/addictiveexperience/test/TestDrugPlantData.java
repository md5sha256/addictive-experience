package com.github.md5sha256.addictiveexperience.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantDataBuilder;
import com.github.md5sha256.addictiveexperience.fixtures.DummyDrugBuilder;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.IStopwatch;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestDrugPlantData {

    private static ServerMock mock;
    private static IDrug drug;
    private static DrugPlantMeta meta;
    private static World world;

    @BeforeAll
    public static void init() {
        mock = MockBukkit.mock();
        world = new WorldMock();
        drug = new DummyDrugBuilder(mock.getItemFactory())
                .material(Material.IRON_INGOT)
                .name("test")
                .permission("permission")
                .build();
        meta = DrugPlantMeta.builder()
                            .result(drug)
                            .growthTime(5, TimeUnit.MINUTES)
                            .harvestAmount(10)
                            .harvestProbability(0.5)
                            .seedDropAmount(5)
                            .seedDropProbability(0.25)
                            .build();
    }

    @AfterAll
    public static void cleanup() {
        MockBukkit.unmock();
    }

    @Test
    public void testSimilarity() {
        final DrugPlantData data = DrugPlantData.builder()
                                                .position(new BlockPosition(world, 1, 1, 1))
                                                .meta(meta)
                                                .build();
        Assertions.assertTrue(data.isSimilar(data));
    }

    @Test
    public void testCloning() {
        final DrugPlantDataBuilder builder = DrugPlantData.builder()
                                                          .position(new BlockPosition(world,
                                                                                      1,
                                                                                      1,
                                                                                      1))
                                                          .meta(meta);

        final DrugPlantDataBuilder copy = new DrugPlantDataBuilder(builder);
        Assertions.assertTrue(builder.build().isSimilar(copy.build()));
        final DrugPlantData data = builder.build();
        Assertions.assertTrue(data.isSimilar(data.toBuilder().build()));
    }

    @Test
    public void testDrugPlantDataGetters() {
        final IStopwatch stopwatch = Stopwatches.variableStopwatch(GuavaAdapter.ofStarted());
        final DrugPlantData data = DrugPlantData.builder()
                                                .meta(meta)
                                                .elapsed(stopwatch)
                                                .position(new BlockPosition(world, 1, 1, 1))
                                                .build();
        Assertions.assertSame(meta, data.meta());
        Assertions.assertNotSame(stopwatch, data.elapsed());
        final DrugPlantData wrappedStopwatchData = data.toBuilder()
                                                       .elapsed(GuavaAdapter.ofUnstarted())
                                                       .build();
        Assertions.assertNotSame(stopwatch, wrappedStopwatchData.elapsed());
        Assertions.assertTrue(wrappedStopwatchData.elapsed().isRunning());
    }


}
