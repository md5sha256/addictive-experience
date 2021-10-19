package com.github.md5sha256.addictiveexperience.api.enchantment;

import com.github.md5sha256.spigotutils.DeregisterableListener;
import com.github.md5sha256.spigotutils.serial.Registry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IEnchantmentHandler extends DeregisterableListener {
    void update();

    void update(@NotNull Entity entity);

    void saveData();

    void saveData(@NotNull Entity holder);

    void loadData(@NotNull Entity holder);

    @NotNull Registry<Key, CustomEnchantment> registry();

    void applyEnchantment(
            @NotNull Entity entity,
            @NotNull CustomEnchantment customEnchantment
    );

    void applyEnchantment(
            @NotNull Entity entity,
            @NotNull CustomEnchantment customEnchantment,
            long durationMillis);

    void removeEnchantment(
            @NotNull Entity entity,
            @NotNull CustomEnchantment customEnchantment
    );

    @NotNull Collection<CustomEnchantment> enchantments(@NotNull Entity holder);

    @NotNull Optional<@NotNull EnchantmentData> enchantmentData(
            @NotNull Entity holder,
            @NotNull CustomEnchantment enchantment
    );

    boolean hasEnchantment(@NotNull Entity holder, @NotNull CustomEnchantment enchantment);

    boolean hasEnchantment(@NotNull Entity holder, @NotNull Key key);
}
