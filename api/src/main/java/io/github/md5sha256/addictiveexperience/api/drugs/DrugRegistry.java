package io.github.md5sha256.addictiveexperience.api.drugs;

import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.util.DataKey;
import io.github.md5sha256.addictiveexperience.api.util.KeyRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface DrugRegistry {

    @NotNull Collection<@NotNull IDrug> drugs();

    @NotNull Collection<@NotNull IDrugComponent> components();

    @NotNull Collection<@NotNull IDrugForm> drugForms();

    void registerComponent(@NotNull IDrugComponent... drugs);

    void registerComponent(@NotNull Collection<? extends @NotNull IDrugComponent> drugs);

    void registerDrugForm(@NotNull IDrugForm... drugForms);

    void registerDrugForms(@NotNull Collection<@NotNull IDrugForm> drugForms);

    @NotNull Optional<@NotNull IDrug> drugByKey(@NotNull Key key);

    @NotNull Optional<@NotNull IDrugComponent> componentByKey(@NotNull Key key);

    @NotNull Optional<@NotNull IDrugForm> formByKey(@NotNull Key key);

    @NotNull Map<Key, IDrug> drugByKeys(@NotNull Collection<@NotNull Key> keys);

    @NotNull Map<Key, IDrugComponent> componentByKeys(@NotNull Collection<@NotNull Key> keys);

    @NotNull Map<Key, IDrugForm> formByKeys(@NotNull Collection<@NotNull Key> keys);

    @NotNull <T> Optional<T> metaData(@NotNull IDrugComponent component, @NotNull DataKey<T> key);

    <T> void metaData(@NotNull IDrugComponent component, @NotNull DataKey<T> key, @NotNull T value);

    <T> void removeMetaData(@NotNull IDrugComponent component, @NotNull DataKey<T> key);

    <T> void removeMetaData(@NotNull DataKey<T> key);

    @NotNull Optional<@NotNull DrugItemData> dataFor(@NotNull ItemStack item);

    @NotNull Optional<@NotNull IDrugComponent> componentFromItem(@NotNull ItemStack itemStack);

    default @NotNull Optional<@NotNull IDrug> drugFromItem(@NotNull ItemStack itemStack) {
        final Optional<IDrugComponent> component = componentFromItem(itemStack);
        if (component.isEmpty()) {
            return Optional.empty();
        }
        final IDrugComponent drugComponent = component.get();
        if (drugComponent instanceof IDrug) {
            return Optional.of((IDrug) drugComponent);
        }
        return Optional.empty();
    }

    @NotNull ItemStack itemForComponent(@NotNull IDrugComponent component);

    @NotNull ItemStack itemForDrug(@NotNull IDrug drug, @NotNull IDrugForm form);

    @NotNull KeyRegistry keysForDrugs();

    @NotNull KeyRegistry keysForComponents();

    @NotNull KeyRegistry keysForForms();

}
