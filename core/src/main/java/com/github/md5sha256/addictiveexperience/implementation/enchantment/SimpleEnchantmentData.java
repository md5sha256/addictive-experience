package com.github.md5sha256.addictiveexperience.implementation.enchantment;

import com.github.md5sha256.addictiveexperience.api.enchantment.EnchantmentData;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.concurrent.TimeUnit;

@ConfigSerializable
public class SimpleEnchantmentData implements EnchantmentData {

    @Setting
    @Required
    private VariableStopwatch elapsed;


    @Setting
    @Required
    private long durationMillis;

    SimpleEnchantmentData() {

    }

    public SimpleEnchantmentData(@NotNull VariableStopwatch stopwatch, long durationMillis) {
        this.elapsed = stopwatch;
        this.durationMillis = durationMillis;
    }

    @Override
    public @NotNull VariableStopwatch elapsed() {
        return this.elapsed;
    }

    @Override
    public long durationMillis() {
        return this.durationMillis;
    }

    @Override
    public long duration(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(durationMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public long remainingDurationMillis() {
        return Math.max(0, this.elapsed.elapsedMillis() - this.durationMillis);
    }

    @Override
    public long remainingDuration(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(remainingDurationMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "EnchantmentData{" +
                ", elapsed=" + elapsed +
                ", durationMillis=" + durationMillis +
                '}';
    }
}
