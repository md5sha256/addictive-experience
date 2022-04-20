package io.github.md5sha256.addictiveexperience.implementation.plant;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface PlantDataResolverFactory {

    @NotNull PlantDataResolver createResolverForWorld(@NotNull World world);

}
