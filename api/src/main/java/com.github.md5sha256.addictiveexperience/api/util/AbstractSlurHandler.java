package com.github.md5sha256.addictiveexperience.api.util;

import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSlurHandler {

    protected final SlurEffectState slurEffectState;
    private final BukkitTask task;

    protected AbstractSlurHandler(@NotNull Plugin plugin,
                                  long intervalTicks,
                                  @NotNull SlurEffectState slurEffectState) {
        this.slurEffectState = slurEffectState;
        this.task = Bukkit.getScheduler()
                          .runTaskTimer(plugin, this::updateState, intervalTicks, intervalTicks);
    }

    public void shutdown() {
        if (!this.task.isCancelled()) {
            this.task.cancel();
        }
    }

    public final boolean isShutdown() {
        return this.task.isCancelled();
    }

    protected void updateState() {
        this.slurEffectState.update();
    }

}
