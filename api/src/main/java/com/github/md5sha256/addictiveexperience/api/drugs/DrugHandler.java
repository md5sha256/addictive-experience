package com.github.md5sha256.addictiveexperience.api.drugs;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface DrugHandler {

    @NotNull Optional<@NotNull DrugBloodData> bloodData(@NotNull UUID player);

    @NotNull DrugBloodData getOrCreateBloodData(@NotNull UUID player);

    void clearData(@NotNull UUID player);

    @NotNull DrugCooldownData cooldownData();

    boolean overdose(@NotNull LivingEntity player, @NotNull IDrug drug);

    void notifyIfOverdosed(@NotNull LivingEntity player, @NotNull IDrug drug);


}
