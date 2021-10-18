package com.github.md5sha256.addictiveexperience.implementation.plant;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SimplePlantDataResolverFactory implements PlantDataResolverFactory {

    private final DrugRegistry drugRegistry;
    private final Plugin plugin;

    public SimplePlantDataResolverFactory(
            @NotNull Plugin plugin,
            @NotNull DrugRegistry drugRegistry
    ) {
        this.plugin = plugin;
        this.drugRegistry = drugRegistry;
    }

    @Override
    public @NotNull PlantDataResolver createResolverForWorld(@NotNull World world) {
        return new PDCResolver(plugin, drugRegistry, world);
    }
}
