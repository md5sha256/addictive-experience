package io.github.md5sha256.addictiveexperience;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantMetaBuilder;
import io.github.md5sha256.addictiveexperience.fixtures.DummyDrugBuilder;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestDrugPlantMeta {

    private static IDrug drug;

    @BeforeAll
    public static void init() {
        ServerMock mock = MockBukkit.mock();
        drug = new DummyDrugBuilder(mock.getItemFactory())
                .material(Material.IRON_NUGGET)
                .name("test")
                .permission("permission")
                .build();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    public void testSimilarity() {
        final DrugPlantMeta meta = DrugPlantMeta.builder()
                                                .result(drug)
                                                .growthTime(5, TimeUnit.MINUTES)
                                                .harvestAmount(10)
                                                .harvestProbability(0.5)
                                                .seedDropAmount(5)
                                                .seedDropProbability(0.25)
                                                .seed(null)
                                                .build();
        Assertions.assertTrue(meta.isSimilar(meta));
    }

    @Test
    public void testCloning() {
        final DrugPlantMetaBuilder builder = DrugPlantMeta.builder()
                                                          .result(drug)
                                                          .growthTime(5, TimeUnit.MINUTES)
                                                          .harvestAmount(10)
                                                          .harvestProbability(0.5)
                                                          .seedDropAmount(5)
                                                          .seedDropProbability(0.25)
                                                          .seed(null);
        final DrugPlantMetaBuilder copy = new DrugPlantMetaBuilder(builder);
        Assertions.assertTrue(builder.build().isSimilar(copy.build()));
        final DrugPlantMeta meta = builder.build();
        Assertions.assertTrue(meta.isSimilar(meta.toBuilder().build()));
    }

    @Test
    public void testDrugPlantMetaGetters() {
        final DrugPlantMeta meta = DrugPlantMeta.builder()
                                                .result(drug)
                                                .growthTime(5, TimeUnit.MINUTES)
                                                .harvestAmount(10)
                                                .harvestProbability(0.5)
                                                .seedDropAmount(5)
                                                .seedDropProbability(0.25)
                                                .seed(null)
                                                .build();
        Assertions.assertSame(drug, meta.result());
        Assertions.assertEquals(5, meta.growthTime(TimeUnit.MINUTES));
        Assertions.assertEquals(TimeUnit.MINUTES.toMillis(5), meta.growthTimeMillis());
        Assertions.assertEquals(10, meta.harvestAmount());
        Assertions.assertEquals(0.5, meta.harvestSuccessProbability());
        Assertions.assertEquals(5, meta.seedDropAmount());
        Assertions.assertEquals(0.25, meta.seedDropProbability());
        Assertions.assertFalse(meta.seed().isPresent());
    }

    @Test
    public void testBuilderValidation() {
        final DrugPlantMeta valid = DrugPlantMeta.builder()
                .result(drug)
                .growthTime(5, TimeUnit.MINUTES)
                .harvestAmount(10)
                .harvestProbability(0.5)
                .seedDropAmount(5)
                .seedDropProbability(0.25)
                .seed(null)
                .build();
        final DrugPlantMetaBuilder invalidResult = valid.toBuilder().result(null);
        Assertions.assertThrows(IllegalArgumentException.class, invalidResult::build);
        final DrugPlantMetaBuilder invalidGrowthTime = valid.toBuilder().growthTimeMillis(-1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidGrowthTime::build);
        final DrugPlantMetaBuilder invalidHarvestAmount = valid.toBuilder().harvestAmount(-1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidHarvestAmount::build);
        final DrugPlantMetaBuilder invalidSeedDropAmount = valid.toBuilder().seedDropAmount(-1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidSeedDropAmount::build);
        final DrugPlantMetaBuilder invalidSeedDropProbability = valid.toBuilder().seedDropProbability(-0.1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidSeedDropProbability::build);
        invalidSeedDropProbability.seedDropProbability(1.1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidSeedDropProbability::build);
        final DrugPlantMetaBuilder invalidHarvestProbability = valid.toBuilder().harvestProbability(-0.1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidHarvestProbability::build);
        invalidHarvestProbability.harvestProbability(1.1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidHarvestProbability::build);
    }

}
