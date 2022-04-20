package io.github.md5sha256.addictiveexperience.implementation.effects;

import io.github.md5sha256.addictiveexperience.api.effect.EffectData;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CustomEffectResolver {

    @NotNull Map<Key, EffectData> loadData(@NotNull PersistentDataHolder holder);

    void clearData(@NotNull PersistentDataHolder holder);

    void saveData(@NotNull PersistentDataHolder holder, @NotNull Map<Key, EffectData> dataMap);
}
