package io.github.md5sha256.addictiveexperience.implementation.drugs.effects;

import io.github.md5sha256.addictiveexperience.api.effect.CustomEffect;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

@Singleton
public class EffectRandomDeath extends EffectRandomAction {

    private final Plugin plugin;

    @Inject
    EffectRandomDeath(
            @NotNull Plugin plugin,
            @NotNull Server server,
            @NotNull BukkitScheduler scheduler) {
        super(
                server,
                scheduler,
                new NamespacedKey(plugin, "random-death"),
                "Random Death",
                Component.text("Random Death")
        );
        this.plugin = plugin;
    }

    public @NotNull CustomEffect createEffect(
            long intervalTicks,
            long durationMillis,
            double chance
    ) {
        return new EffectImpl(this.plugin, intervalTicks, durationMillis, chance);
    }

    private class EffectImpl extends BaseImpl {

        public EffectImpl(@NotNull Plugin plugin,
                          long intervalTicks,
                          long durationMillis,
                          double chance) {
            super(plugin, intervalTicks, durationMillis, chance);
        }

        @Override
        protected void activate(@NotNull LivingEntity livingEntity) {
            // Effectively kill
            livingEntity.remove();
        }

    }


}
