package com.github.md5sha256.addictiveexperience.implementation.drugs.effects;

import com.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import com.github.md5sha256.addictiveexperience.api.effect.EffectData;
import com.github.md5sha256.addictiveexperience.implementation.effects.SimpleEffectData;
import com.github.md5sha256.spigotutils.timing.BukkitTimer;
import com.github.md5sha256.spigotutils.timing.SimpleTimerData;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class EffectRandomAction extends SimplePassiveEffect {

    protected final Random random = new Random();
    protected final Server server;
    protected final BukkitScheduler scheduler;


    protected EffectRandomAction(
            @NotNull Server server,
            @NotNull BukkitScheduler scheduler,
            @NotNull Key key,
            @NotNull String name,
            @NotNull Component displayName
    ) {
        super(key, name, displayName);
        this.server = server;
        this.scheduler = scheduler;
    }

    public abstract class BaseImpl extends BukkitTimer<UUID, EffectData, SimpleTimerData<EffectData>>
            implements CustomEffect {

        private final long durationMillis;
        private final double chance;

        protected BaseImpl(
                @NotNull Plugin plugin,
                long intervalTicks,
                long durationMillis,
                double chance
        ) {
            super(plugin, scheduler, false, intervalTicks);
            this.durationMillis = durationMillis;
            this.chance = chance;
        }

        @Override
        protected synchronized void update() {
            super.update();
        }

        protected abstract void activate(@NotNull LivingEntity livingEntity);

        @Override
        protected void onRemove(@NotNull UUID key, @NotNull SimpleTimerData<EffectData> value) {

        }

        @Override
        protected @NotNull SimpleTimerData<EffectData> create(@NotNull UUID key,
                                                              @NotNull EffectData value) {
            return new SimpleTimerData<>(value, Stopwatches.newInstance());
        }

        private boolean shouldActivate() {
            return EffectRandomAction.this.random.nextDouble() <= this.chance;
        }

        private @NotNull EffectData createEffectData() {
            final VariableStopwatch stopwatch = Stopwatches.variableStopwatch(Stopwatches.newInstance());
            return new SimpleEffectData(stopwatch, this.durationMillis);
        }

        @Override
        protected boolean shouldRemove(Map.@NotNull Entry<UUID, SimpleTimerData<EffectData>> entry) {
            if (shouldActivate()) {
                final Entity entity = EffectRandomAction.this.server.getEntity(entry.getKey());
                if (entity instanceof LivingEntity livingEntity) {
                    activate(livingEntity);
                }
            }
            return entry.getValue().data().remainingDurationMillis() == 0;
        }

        @Override
        public @NotNull String name() {
            return EffectRandomAction.this.name();
        }

        @Override
        public @NotNull Component displayName() {
            return EffectRandomAction.this.displayName();
        }

        @Override
        public long durationMillis() {
            return this.durationMillis;
        }

        @Override
        public long duration(@NotNull TimeUnit timeUnit) {
            return timeUnit.convert(this.durationMillis, TimeUnit.MILLISECONDS);
        }

        @Override
        public @NotNull Key key() {
            return EffectRandomAction.this.key();
        }

        @Override
        public void applyTo(@NotNull LivingEntity entity) {
            submit(entity.getUniqueId(), createEffectData());
        }

        @Override
        public void removeFrom(@NotNull LivingEntity entity) {
            remove(entity.getUniqueId());
        }
    }

}
