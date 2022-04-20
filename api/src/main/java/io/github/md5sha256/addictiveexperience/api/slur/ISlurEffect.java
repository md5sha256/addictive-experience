package io.github.md5sha256.addictiveexperience.api.slur;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ISlurEffect {

    @NotNull ISlurEffect NONE = (player, message) -> message;

    @NotNull Component formatMessage(@NotNull Player player, @NotNull Component message);

}
