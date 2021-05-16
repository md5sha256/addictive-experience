package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.util.DataKey;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DrugRegistry {

    @NotNull Set<@NotNull IDrug> drugs();

    @NotNull Set<@NotNull IDrugForm> drugForms();

    @NotNull Optional<@NotNull DrugItemData> dataFor(@NotNull ItemStack item);

    void registerDrug(@NotNull IDrug... drugs);

    void registerDrug(@NotNull Collection<@NotNull IDrug> drugs);

    void registerDrugForm(@NotNull IDrugForm... drugForms);

    void registerDrugForms(@NotNull Collection<@NotNull IDrugForm> drugForms);

    @NotNull Optional<@NotNull IDrug> drugByKey(@NotNull Key key);

    @NotNull Map<Key, IDrug> drugByKeys(@NotNull Collection<Key> keys);

    @NotNull <T> Optional<T> metaData(@NotNull IDrug drug, @NotNull DataKey<T> key);

    <T> void metaData(@NotNull IDrug drug, @NotNull DataKey<T> key, @NotNull T value);

    <T> void removeMetaData(@NotNull IDrug drug, @NotNull DataKey<T> key);

    <T> void removeMetaData(@NotNull DataKey<T> key);

}
