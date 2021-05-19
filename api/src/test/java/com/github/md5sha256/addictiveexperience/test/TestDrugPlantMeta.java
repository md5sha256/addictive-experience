package com.github.md5sha256.addictiveexperience.test;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugPlantMetaBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestDrugPlantMeta {

    @Test
    public void testSimilarity() {
        final DrugPlantMeta meta = DrugPlantMeta.builder()
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
                                                .growthTime(5, TimeUnit.MINUTES)
                                                .harvestAmount(10)
                                                .harvestProbability(0.5)
                                                .seedDropAmount(5)
                                                .seedDropProbability(0.25)
                                                .seed(null)
                                                .build();
        Assertions.assertEquals(5, meta.growthTime(TimeUnit.MINUTES));
        Assertions.assertEquals(TimeUnit.MINUTES.toMillis(5), meta.growthTimeMillis());
        Assertions.assertEquals(10, meta.harvestAmount());
        Assertions.assertEquals(0.5, meta.harvestSuccessProbability());
        Assertions.assertEquals(5, meta.seedDropAmount());
        Assertions.assertEquals(0.25, meta.seedDropProbability());
        Assertions.assertFalse(meta.seed().isPresent());
    }


}
