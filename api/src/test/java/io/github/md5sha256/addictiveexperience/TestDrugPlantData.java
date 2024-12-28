package io.github.md5sha256.addictiveexperience;

import io.github.md5sha256.addictiveexperience.fixtures.DummyDrugForm;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantDataBuilder;
import io.github.md5sha256.addictiveexperience.fixtures.DummyDrugBuilder;
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

    private static DrugPlantMeta meta;
    private static World world;

    @BeforeAll
    public static void init() {
        ServerMock mock = MockBukkit.mock();
        world = new WorldMock();
        IDrug drug = new DummyDrugBuilder(mock.getItemFactory())
                .material(Material.IRON_INGOT)
                .name("test")
                .permission("permission")
                .drugForm(new DummyDrugForm(mock.getItemFactory()))
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
    public static void teardown() {
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
        final BlockPosition position = new BlockPosition(world, 1, 1, 1);
        final DrugPlantDataBuilder builder = DrugPlantData.builder()
                                                          .position(position)
                                                          .meta(meta);

        final DrugPlantDataBuilder copy = new DrugPlantDataBuilder(builder);
        final DrugPlantData built = copy.build();
        built.elapsed().stop();
        Assertions.assertTrue(built.isSimilar(copy.build()));
        final DrugPlantData data = builder.build();
        Assertions.assertTrue(data.isSimilar(data.toBuilder().build()));
    }

    @Test
    public void testBuilderValidation() {
        final BlockPosition position = new BlockPosition(world, 1, 1, 1);
        final DrugPlantDataBuilder emptyStopwatch = DrugPlantData.builder()
                .elapsed(null)
                .position(position)
                .meta(meta)
                .startTimeEpochMilli(0);
        try {
            DrugPlantData plantData = emptyStopwatch.build();
            Assertions.assertNotNull(plantData.elapsed());
        } catch (Exception ex) {
            Assertions.fail(ex);
            return;
        }
        final DrugPlantDataBuilder invalidStart = DrugPlantData.builder()
                .position(position)
                .meta(meta)
                .startTimeEpochMilli(-1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidStart::build, "Invalid start time");
        final DrugPlantDataBuilder invalidPosition = DrugPlantData.builder()
                .position(null)
                .meta(meta)
                .startTimeEpochMilli(0);
        Assertions.assertThrows(NullPointerException.class, invalidPosition::build, "Position cannot be null");
        final DrugPlantDataBuilder invalidMeta = DrugPlantData.builder()
                .position(position)
                .meta(null)
                .startTimeEpochMilli(0);
        Assertions.assertThrows(NullPointerException.class, invalidMeta::build, "Meta cannot be null");
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
