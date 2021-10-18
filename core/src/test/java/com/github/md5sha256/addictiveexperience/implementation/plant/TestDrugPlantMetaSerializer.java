package com.github.md5sha256.addictiveexperience.implementation.plant;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.fixtures.DummyDrugImpl;
import com.github.md5sha256.addictiveexperience.implementation.drugs.SimpleDrugRegistry;
import com.github.md5sha256.addictiveexperience.util.configurate.AdventureKeySerializer;
import com.github.md5sha256.addictiveexperience.util.configurate.DrugPlantMetaSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

public class TestDrugPlantMetaSerializer {

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
        IDrug mockDrug = new DummyDrugImpl(
                mock.getItemFactory(),
                Key.key("dummy", "dummy-drug"),
                "dummy drug",
                Material.IRON_AXE,
                "");
        DrugPlantMeta plantMeta = DrugPlantMeta.defaultMeta(mockDrug);
        DrugRegistry drugRegistry = new SimpleDrugRegistry(plugin);
        drugRegistry.registerComponent(mockDrug);
        DrugPlantMetaSerializer serializer = new DrugPlantMetaSerializer(drugRegistry);
        ConfigurationLoader<?> loader = GsonConfigurationLoader.builder()
                .defaultOptions(options -> options.serializers(serializers -> {
                    serializers.register(DrugPlantMeta.class, serializer);
                    serializers.register(Key.class, new AdventureKeySerializer());
                }))
                .build();
        ConfigurationNode node = loader.createNode();
        DrugPlantMeta deserialized;
        try {
            node.set(plantMeta);
            deserialized = node.get(DrugPlantMeta.class);
        } catch (SerializationException ex) {
            Assertions.fail(ex);
            return;
        }
        Assertions.assertNotNull(deserialized);
        Assertions.assertTrue(plantMeta.isSimilar(deserialized));
    }


}
