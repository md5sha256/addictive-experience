package com.github.md5sha256.addictiveexperience.implementation.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import com.github.md5sha256.addictiveexperience.api.util.DataKey;
import com.github.md5sha256.spigotutils.serial.Registry;
import com.github.md5sha256.spigotutils.serial.SimpleRegistry;
import com.google.inject.Singleton;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public final class SimpleDrugRegistry implements DrugRegistry {

    private final Registry<Key, IDrug> drugRegistry = new SimpleRegistry<>();
    private final Registry<Key, IDrugComponent> componentRegistry = new SimpleRegistry<>();
    private final Registry<Key, IDrugForm> formRegistry = new SimpleRegistry<>();

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
        return Optional.empty();
    }

    @Override
    public void registerComponent(final @NotNull IDrugComponent... drugs) {
        for (IDrugComponent component : drugs) {
            if (component instanceof IDrug) {
                this.drugRegistry.register(component.key(), (IDrug) component);
            }
            this.componentRegistry.register(component.key(), component);
        }
    }

    @Override
    public void registerComponent(@NotNull final Collection<? extends @NotNull IDrugComponent> drugs) {
        for (IDrugComponent component : drugs) {
            if (component instanceof IDrug) {
                this.drugRegistry.register(component.key(), (IDrug) component);
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
    public @NotNull <T> Optional<T> metaData(@NotNull final IDrugComponent component,
                                             @NotNull final DataKey<T> key) {
        return Optional.empty();
    }

    @Override
    public <T> void metaData(@NotNull final IDrugComponent component,
                             @NotNull final DataKey<T> key,
                             @NotNull final T value) {

    }

    @Override
    public <T> void removeMetaData(@NotNull final IDrugComponent component,
                                   @NotNull final DataKey<T> key) {

    }

    @Override
    public <T> void removeMetaData(@NotNull final DataKey<T> key) {

    }
}
