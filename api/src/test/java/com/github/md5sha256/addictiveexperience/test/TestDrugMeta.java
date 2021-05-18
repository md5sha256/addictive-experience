package com.github.md5sha256.addictiveexperience.test;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugMetaBuilder;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TestDrugMeta {

    private static void assertSimilar(@NotNull DrugMeta meta1, @NotNull DrugMeta meta2) {
        Assertions.assertEquals(meta1.drugEnabled(), meta2.drugEnabled());
        Assertions.assertEquals(meta1.effect(), meta2.effect());
        final Set<PotionEffect> effects = new HashSet<>(meta1.effects());
        final Set<PotionEffect> effects2 = meta2.effects();
        Assertions.assertEquals(effects.size(), effects2.size());
        Assertions.assertFalse(effects.addAll(effects2));
    }

    @Test
    public void testMetaKey() {
        Assertions.assertEquals("DrugMeta", DrugMeta.KEY.name());
        Assertions.assertEquals(DrugMeta.class, DrugMeta.KEY.type().getType());
    }

    @Test
    public void testMetaCloning() {
        final DrugMetaBuilder builder = DrugMeta.builder()
                                                .enabled(false)
                                                .overdoseThreshold(10)
                                                .slurDurationMillis(1000);
        final DrugMetaBuilder copy = new DrugMetaBuilder(builder);
        assertSimilar(builder.build(), copy.build());
        final DrugMeta meta = builder.build();
        assertSimilar(meta, meta.toBuilder().build());
    }

    @Test
    public void testMetaBuilderValidation() {
        final DrugMetaBuilder builder = DrugMeta.builder();
        Assertions.assertThrows(NullPointerException.class,
                                () -> builder.effects((Collection<PotionEffect>) null));
        Assertions.assertThrows(NullPointerException.class,
                                () -> builder.effects((PotionEffect[]) null));
        final DrugMetaBuilder valid = builder.enabled(false)
                                             .overdoseThreshold(10)
                                             .slurDurationMillis(1000);
        try {
            valid.build();
        } catch (Exception ex) {
            Assertions.fail(ex);
            return;
        }
        final DrugMetaBuilder invalidOverdose = new DrugMetaBuilder(valid).overdoseThreshold(0);
        Assertions.assertThrows(IllegalArgumentException.class, invalidOverdose::build);
        final DrugMetaBuilder invalidSlurDuration = new DrugMetaBuilder(valid)
                .slurDurationMillis(-1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidSlurDuration::build);
    }

    @Test
    public void testMetaGetters() {
        final DrugMeta meta = DrugMeta.builder()
                                      .enabled(false)
                                      .overdoseThreshold(10)
                                      .slurDurationMillis(1000)
                                      .build();
        Assertions.assertFalse(meta.drugEnabled());
        Assertions.assertEquals(10, meta.overdoseThreshold());
        Assertions.assertEquals(1000, meta.slurDurationMillis());
        Assertions.assertEquals(1, meta.slurDuration(TimeUnit.SECONDS));
        Assertions.assertTrue(meta.effects().isEmpty());
        Assertions.assertFalse(meta.effect().isPresent());
    }

}
