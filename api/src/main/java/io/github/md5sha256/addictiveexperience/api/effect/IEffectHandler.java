package io.github.md5sha256.addictiveexperience.api.effect;

import com.github.md5sha256.spigotutils.DeregisterableListener;
import com.github.md5sha256.spigotutils.serial.Registry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IEffectHandler extends DeregisterableListener {

    void update();

    void update(@NotNull LivingEntity entity);

    void saveData();

    void saveData(@NotNull LivingEntity holder);

    void loadData(@NotNull LivingEntity holder);

    @NotNull Registry<Key, CustomEffect> registry();

    void applyEffect(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect customEffect
    );

    void applyEffect(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect customEffect,
            long durationMillis);

    void removeEffect(
            @NotNull LivingEntity entity,
            @NotNull CustomEffect customEffect
    );

    void shutdown();

    @NotNull Collection<CustomEffect> enchantments(@NotNull LivingEntity holder);

    @NotNull Optional<@NotNull EffectData> enchantmentData(
            @NotNull LivingEntity holder,
            @NotNull CustomEffect enchantment
    );

    boolean hasEnchantment(@NotNull LivingEntity holder, @NotNull CustomEffect enchantment);

    boolean hasEnchantment(@NotNull LivingEntity holder, @NotNull Key key);
}
