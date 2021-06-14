package com.github.md5sha256.addictiveexperience.implementation.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.util.DataKey;
import com.github.md5sha256.addictiveexperience.configuration.DrugConfiguration;
import com.github.md5sha256.addictiveexperience.util.DataMapper;
import com.github.md5sha256.spigotutils.serial.Registry;
import com.github.md5sha256.spigotutils.serial.SimpleRegistry;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Singleton
public final class SimpleDrugRegistry implements DrugRegistry {

    private final Registry<Key, IDrug> drugRegistry = new SimpleRegistry<>();
    private final Registry<Key, IDrugComponent> componentRegistry = new SimpleRegistry<>();
    private final Registry<Key, IDrugForm> formRegistry = new SimpleRegistry<>();
    private final DrugItemDataFactory itemDataFactory;
    private final DataMapper<IDrugComponent> dataMapper = new DataMapper<>();

    @Inject
    public SimpleDrugRegistry(@NotNull Plugin plugin) {
        this.itemDataFactory = new SimpleDrugItemDataFactory(plugin, this);
    }

    public void registerFromConfiguration(@NotNull DrugConfiguration configuration) {
        for (Map.Entry<IDrug, DrugMeta> entry : configuration.drugMeta().entrySet()) {
            final IDrug drug = entry.getKey();
            final DrugMeta meta = entry.getValue();
            registerComponent(drug);
            this.dataMapper.set(drug, DrugMeta.KEY, meta);
        }
    }

    @Override
    public @NotNull Collection<@NotNull IDrug> drugs() {
        return this.drugRegistry.values();
    }

    @Override
    public @NotNull Collection<@NotNull IDrugComponent> components() {
        return this.componentRegistry.values();
    }

    @Override
    public @NotNull Collection<@NotNull IDrugForm> drugForms() {
        return this.formRegistry.values();
    }

    @Override
    public @NotNull Optional<@NotNull DrugItemData> dataFor(@NotNull final ItemStack item) {
        return this.itemDataFactory.dataFor(item);
    }

    @Override
    public void registerComponent(@NotNull IDrugComponent... drugs) {
        registerComponent(Arrays.asList(Objects.requireNonNull(drugs)));
    }

    @Override
    public void registerComponent(@NotNull final Collection<? extends @NotNull IDrugComponent> drugs) {
        for (IDrugComponent component : drugs) {
            if (component instanceof IDrug) {
                final IDrug drug = (IDrug) component;
                this.drugRegistry.register(component.key(), drug);
                this.dataMapper.set(drug, DrugMeta.KEY, drug.defaultMeta());
            }
            this.componentRegistry.register(component.key(), component);
        }
    }

    @Override
    public void registerDrugForm(final @NotNull IDrugForm... drugForms) {
        for (IDrugForm form : drugForms) {
            this.formRegistry.register(form.key(), form);
        }
    }

    @Override
    public void registerDrugForms(@NotNull final Collection<@NotNull IDrugForm> drugForms) {
        for (IDrugForm form : drugForms) {
            this.formRegistry.register(form.key(), form);
        }
    }

    @Override
    public @NotNull Optional<@NotNull IDrug> drugByKey(@NotNull final Key key) {
        return this.drugRegistry.get(key);
    }

    @Override
    public @NotNull Optional<@NotNull IDrugComponent> componentByKey(@NotNull final Key key) {
        return this.componentRegistry.get(key);
    }

    @Override
    public @NotNull Optional<@NotNull IDrugForm> formByKey(@NotNull final Key key) {
        return this.formRegistry.get(key);
    }

    @Override
    public @NotNull Map<Key, IDrug> drugByKeys(@NotNull final Collection<Key> keys) {
        final Map<Key, IDrug> map = new HashMap<>(keys.size());
        for (Key key : keys) {
            final Optional<IDrug> optional = this.drugRegistry.get(key);
            optional.ifPresent(drug -> map.put(key, drug));
        }
        return map;
    }

    @Override
    public @NotNull Map<Key, IDrugComponent> componentByKeys(@NotNull final Collection<Key> keys) {
        final Map<Key, IDrugComponent> map = new HashMap<>(keys.size());
        for (Key key : keys) {
            final Optional<IDrugComponent> optional = this.componentRegistry.get(key);
            optional.ifPresent(drug -> map.put(key, drug));
        }
        return map;
    }

    @Override
    public @NotNull Map<Key, IDrugForm> formByKeys(@NotNull final Collection<@NotNull Key> keys) {
        final Map<Key, IDrugForm> map = new HashMap<>(keys.size());
        for (Key key : keys) {
            final Optional<IDrugForm> optional = this.formRegistry.get(key);
            optional.ifPresent(drug -> map.put(key, drug));
        }
        return map;
    }

    @Override
    public @NotNull <T> Optional<T> metaData(@NotNull final IDrugComponent component,
                                             @NotNull final DataKey<T> key) {
        return this.dataMapper.get(component, key);
    }

    @Override
    public <T> void metaData(@NotNull final IDrugComponent component,
                             @NotNull final DataKey<T> key,
                             @NotNull final T value) {
        this.dataMapper.set(component, key, value);
    }

    @Override
    public <T> void removeMetaData(@NotNull final IDrugComponent component,
                                   @NotNull final DataKey<T> key) {
        this.dataMapper.clear(component, key);
    }

    @Override
    public <T> void removeMetaData(@NotNull final DataKey<T> key) {
        this.dataMapper.clear(key);
    }

    @Override
    public @NotNull Optional<@NotNull IDrugComponent> componentFromItem(@NotNull final ItemStack itemStack) {
        return this.itemDataFactory.dataFor(itemStack).map(DrugItemData::component);
    }

    @Override
    public @NotNull ItemStack itemForComponent(@NotNull IDrugComponent component,
                                               @NotNull IDrugForm form) {
        final ItemStack itemStack = component.asItem();
        this.itemDataFactory.data(itemStack, DrugItemData.of(component, form));
        return itemStack;
    }
}
