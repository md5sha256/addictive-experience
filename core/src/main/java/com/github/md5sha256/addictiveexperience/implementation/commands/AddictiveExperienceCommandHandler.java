package com.github.md5sha256.addictiveexperience.implementation.commands;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.ProxiedBy;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import com.github.md5sha256.addictiveexperience.implementation.shop.DrugShopUI;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AddictiveExperienceCommandHandler {

    @Inject
    private DrugShopUI drugShopUI;

    @Inject
    public AddictiveExperienceCommandHandler(@NotNull Plugin plugin) {

        /* Create the command manager */
        final PaperCommandManager<CommandSender> manager;

        try {
            manager = new PaperCommandManager<>(
                    plugin,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
        } catch (final Exception e) {
            plugin.getLogger().severe("Failed to enable to command manager: " + e.getMessage());
            return;
        }

        /* Register Brigadier support */
        try {
            manager.registerBrigadier();
        } catch (final Exception e) {
            plugin.getLogger().warning("Failed to enable Brigadier: " + e.getMessage());
        }

        /* Register asynchronous completions */
        try {
            manager.registerAsynchronousCompletions();
        } catch (final Exception e) {
            plugin.getLogger()
                  .warning("Failed to enable asynchronous completions: " + e.getMessage());
        }

        final MinecraftHelp<CommandSender> minecraftHelp = new MinecraftHelp<>(
                "/hyperverse",
                cs -> cs,
                manager
        );
        final AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(
                manager,
                CommandSender.class,
                parameters -> SimpleCommandMeta.empty()
        );
        annotationParser.parse(this);
    }

    @CommandMethod("drugshop")
    @CommandPermission("addictiveexperience.shop")
    @CommandDescription("Open the drug shop")
    public void openDrugShop(Player player) {
        this.drugShopUI.openShop(player);
    }

}
