package com.github.md5sha256.addictiveexperience.implementation.slurs;

import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.google.inject.Inject;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PaperSlurHandler implements Listener {

    @Inject
    private SlurEffectState slurEffectState;

    private static @NotNull Component formatSlur(@NotNull Component displayName, @NotNull Component message) {
        // FIXME slur format
        final Component slurFormat = Component.text("slur format");
        return slurFormat.replaceText(builder -> builder.match("{PLAYER}").replacement(displayName))
                .replaceText(builder -> builder.match("{MESSAGE}").replacement(message));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerChat(@NotNull AsyncChatEvent event) {
        event.renderer(this::composeMessage);
    }

    private @NotNull Component composeMessage(@NotNull Player source,
                                              @NotNull Component sourceDisplayName,
                                              @NotNull Component message,
                                              @NotNull Audience viewer
    ) {
        final Optional<ISlurEffect> optional = this.slurEffectState
                .currentSlurEffect(source.getUniqueId());
        if (!optional.isPresent()) {
            return ChatRenderer.defaultRenderer()
                               .render(source, sourceDisplayName, message, viewer);
        }
        final ISlurEffect effect = optional.get();
        return effect.formatMessage(source, formatSlur(sourceDisplayName, message));
    }

}
