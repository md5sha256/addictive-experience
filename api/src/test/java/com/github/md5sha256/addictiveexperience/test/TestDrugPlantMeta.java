package com.github.md5sha256.addictiveexperience.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantMetaBuilder;
import com.github.md5sha256.addictiveexperience.fixtures.DummyDrugBuilder;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestDrugPlantMeta {

    private static ServerMock mock;
    private static IDrug drug;

    @BeforeAll
    public static void init() {
        mock = MockBukkit.mock();
        drug = new DummyDrugBuilder(mock.getItemFactory())
                .material(Material.IRON_NUGGET)
                .name("test")
                .permission("permission")
                .build();
    }

    @AfterAll
    public static void cleanup() {
        MockBukkit.unmock();
    }

    @Test
    public void testSimilarity() {
        final DrugPlantMeta meta = DrugPlantMeta.builder()
                                                .drug(drug)
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
                                                          .drug(drug)
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
                                                .drug(drug)
                                                .growthTime(5, TimeUnit.MINUTES)
                                                .harvestAmount(10)
                                                .harvestProbability(0.5)
                                                .seedDropAmount(5)
                                                .seedDropProbability(0.25)
                                                .seed(null)
                                                .build();
        Assertions.assertSame(drug, meta.drug());
        Assertions.assertEquals(5, meta.growthTime(TimeUnit.MINUTES));
        Assertions.assertEquals(TimeUnit.MINUTES.toMillis(5), meta.growthTimeMillis());
        Assertions.assertEquals(10, meta.harvestAmount());
        Assertions.assertEquals(0.5, meta.harvestSuccessProbability());
        Assertions.assertEquals(5, meta.seedDropAmount());
        Assertions.assertEquals(0.25, meta.seedDropProbability());
        Assertions.assertFalse(meta.seed().isPresent());
    }


}
