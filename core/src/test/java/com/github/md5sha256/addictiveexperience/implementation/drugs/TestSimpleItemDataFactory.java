package com.github.md5sha256.addictiveexperience.implementation.drugs;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.fixtures.DummyDrugImpl;
import com.github.md5sha256.addictiveexperience.implementation.forms.FormDefault;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestSimpleItemDataFactory {

    private static ServerMock mock;
    private static Plugin plugin;
    private static DrugRegistry drugRegistry;

    @BeforeAll
    public static void init() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin();
        drugRegistry = new SimpleDrugRegistry(plugin);
    }

    @AfterAll
    public static void cleanUp() {
        MockBukkit.unmock();
    }

    @Test
    public void testItemDataSerialization() {
        final IDrug drug = new DummyDrugImpl(
                mock.getItemFactory(),
                Key.key("addictive-experience", "dummy-drug"),
                "dummy-drug",
                Material.IRON_INGOT,
                "permission"
        );
        final IDrugForm form = new FormDefault(mock.getItemFactory());
        drugRegistry.registerDrugForm(form);
        drugRegistry.registerComponent(drug);
        final DrugItemDataFactory itemDataFactory = new SimpleDrugItemDataFactory(plugin, drugRegistry);

        final ItemStack original = new ItemStack(Material.STRING);
        final ItemStack drugEncoded = original.clone();
        itemDataFactory.data(drugEncoded, DrugItemData.of(drug, form));
        final Optional<IDrugComponent> optionalComponent = itemDataFactory.parseComponent(drugEncoded);
        Assertions.assertTrue(optionalComponent.isPresent());
        Assertions.assertEquals(drug, optionalComponent.get());
        final Optional<DrugItemData> optionalItemData = itemDataFactory.parseData(drugEncoded);
        Assertions.assertTrue(optionalItemData.isPresent());
        final DrugItemData data = optionalItemData.get();
        Assertions.assertEquals(drug, data.drug());
        Assertions.assertEquals(form, data.form());
    }


}
