package com.github.md5sha256.addictiveexperience.implementation.enchantment;

import com.github.md5sha256.addictiveexperience.api.enchantment.EnchantmentData;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CustomEnchantmentResolver {

    @NotNull Map<Key, EnchantmentData> loadData(@NotNull PersistentDataHolder holder);

    void clearData(@NotNull PersistentDataHolder holder);

    void saveData(@NotNull PersistentDataHolder holder, @NotNull Map<Key, EnchantmentData> dataMap);
}
