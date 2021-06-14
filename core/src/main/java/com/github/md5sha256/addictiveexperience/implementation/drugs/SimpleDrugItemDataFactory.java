package com.github.md5sha256.addictiveexperience.implementation.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class SimpleDrugItemDataFactory implements DrugItemDataFactory {

    private final NamespacedKey keyParent;
    private final NamespacedKey keyForm;
    private final NamespacedKey keyDrugComponent;
    private final DrugRegistry drugRegistry;


    public SimpleDrugItemDataFactory(@NotNull Plugin plugin, @NotNull DrugRegistry drugRegistry) {
        this.keyParent = new NamespacedKey(plugin, "drug-data");
        this.keyForm = new NamespacedKey(plugin, "form");
        this.keyDrugComponent = new NamespacedKey(plugin, "drug-component");
        this.drugRegistry = drugRegistry;
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public @NotNull Optional<DrugItemData> parseData(@NotNull ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return Optional.empty();
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        final PersistentDataContainer parentContainer = meta.getPersistentDataContainer();
        if (!parentContainer.has(this.keyParent, PersistentDataType.TAG_CONTAINER)) {
            return Optional.empty();
        }
        final PersistentDataAdapterContext context = parentContainer.getAdapterContext();
        final PersistentDataContainer container = parentContainer
                .getOrDefault(this.keyParent,
                              PersistentDataType.TAG_CONTAINER,
                              context.newPersistentDataContainer());
        final String identifierForm = container.get(this.keyForm, PersistentDataType.STRING);
        final String identifierComponent = container
                .get(this.keyDrugComponent, PersistentDataType.STRING);
        if (identifierForm == null || identifierComponent == null) {
            return Optional.empty();
        }
        final Key keyForm = Key.key(identifierForm);
        final Key keyComponent = Key.key(identifierComponent);
        final Optional<IDrugForm> optionalDrugForm = this.drugRegistry.formByKey(keyForm);
        final Optional<IDrug> optionalDrug = this.drugRegistry.drugByKey(keyComponent);
        if (!optionalDrugForm.isPresent() || !optionalDrug.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(DrugItemData.of(optionalDrug.get(), optionalDrugForm.get()));
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public @NotNull Optional<IDrugComponent> parseComponent(@NotNull final ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return Optional.empty();
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        final PersistentDataContainer parentContainer = meta.getPersistentDataContainer();
        if (!parentContainer.has(this.keyParent, PersistentDataType.TAG_CONTAINER)) {
            return Optional.empty();
        }
        final PersistentDataAdapterContext context = parentContainer.getAdapterContext();
        final PersistentDataContainer container = parentContainer
                .getOrDefault(this.keyParent,
                              PersistentDataType.TAG_CONTAINER,
                              context.newPersistentDataContainer());
        final String identifierComponent = container
                .get(this.keyDrugComponent, PersistentDataType.STRING);
        if (identifierComponent == null) {
            return Optional.empty();
        }
        final Key keyComponent = Key.key(identifierComponent);
        return this.drugRegistry.componentByKey(keyComponent);
    }

    @Override
    public void data(@NotNull ItemStack itemStack, @NotNull DrugItemData itemData) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        final PersistentDataContainer parent = meta.getPersistentDataContainer();
        final PersistentDataAdapterContext context = parent.getAdapterContext();
        final PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(this.keyDrugComponent,
                      PersistentDataType.STRING,
                      itemData.drug().key().asString());
        container.set(this.keyForm, PersistentDataType.STRING, itemData.form().key().asString());
        parent.set(this.keyParent, PersistentDataType.TAG_CONTAINER, container);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void data(@NotNull final ItemStack itemStack, @NotNull final IDrugComponent component) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        final PersistentDataContainer parent = meta.getPersistentDataContainer();
        final PersistentDataAdapterContext context = parent.getAdapterContext();
        final PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(this.keyDrugComponent, PersistentDataType.STRING, component.key().asString());
        parent.set(this.keyParent, PersistentDataType.TAG_CONTAINER, container);
        itemStack.setItemMeta(meta);
    }
}
