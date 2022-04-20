package io.github.md5sha256.addictiveexperience.implementation.commands;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.Flag;
import cloud.commandframework.annotations.Hidden;
import cloud.commandframework.annotations.ProxiedBy;
import cloud.commandframework.annotations.parsers.Parser;
import cloud.commandframework.annotations.specifier.Range;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import io.github.md5sha256.addictiveexperience.api.util.KeyRegistry;
import io.github.md5sha256.addictiveexperience.implementation.shop.DrugShopUI;
import io.github.md5sha256.addictiveexperience.util.Utils;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddictiveExperienceCommandHandler {

    private final Pattern PATTERN_KEY = Pattern.compile("([a-z0-9_\\-.]+):([a-z0-9_\\-.]+)");


    private final DrugShopUI drugShopUI;
    private final DrugRegistry drugRegistry;
    private final IDrugForms drugForms;

    @Inject
    public AddictiveExperienceCommandHandler(@NotNull Plugin plugin,
                                             @NotNull DrugRegistry drugRegistry,
                                             @NotNull IDrugForms drugForms,
                                             @NotNull DrugShopUI drugShopUI
    ) {
        this.drugShopUI = drugShopUI;
        this.drugForms = drugForms;
        this.drugRegistry = drugRegistry;


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
            // manager.registerBrigadier();
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
                "/addictiveexperience",
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


    @Suggestions("drug-components")
    public @NotNull List<String> suggestDrugComponents(@NotNull CommandContext<CommandSender> context,
                                                       @NotNull String input) {
        return suggestFromKeyRegistry(input, this.drugRegistry.keysForComponents());
    }

    @Suggestions("drugs")
    public @NotNull List<String> suggestDrugs(@NotNull CommandContext<CommandSender> context,
                                              @Nullable String input) {
        return suggestFromKeyRegistry(input, this.drugRegistry.keysForDrugs());
    }

    @Suggestions("drug-forms")
    public @NotNull List<String> suggestForms(@NotNull CommandContext<CommandSender> context,
                                              @Nullable String input) {
        return suggestFromKeyRegistry(input, this.drugRegistry.keysForForms());
    }


    private @NotNull List<@NotNull String> suggestFromKeyRegistry(
            @Nullable String input,
            @NotNull KeyRegistry keyRegistry
    ) {
        if (input == null || input.isEmpty()) {
            return keyRegistry.keys().stream()
                              .sorted(Comparator.reverseOrder())
                              .collect(Collectors.toList());
        }
        final Matcher matcher = PATTERN_KEY.matcher(input);
        if (!matcher.matches()) {
            // Suggest the keys first
            if (input.indexOf(':') == -1) {
                return keyRegistry.keys().stream()
                                  .sorted(Comparator.reverseOrder())
                                  .map(s -> s + ":")
                                  .collect(Collectors.toList());
            }
            // Try to resolve anyway
            final String end = ":" + input;
            final Set<String> keys = keyRegistry.keysForValue(input);
            return keys.stream()
                       .sorted(Comparator.reverseOrder())
                       .map(s -> s + end)
                       .collect(Collectors.toList());
        }
        final String key = matcher.group(1);
        final Set<String> values = keyRegistry.values(key);
        final String value = matcher.group(2);
        Stream<String> stream = values.stream()
                                      .sorted(Comparator.reverseOrder());
        if (value != null && !value.isEmpty()) {
            stream = stream.filter(suggestion ->
                                           value.startsWith(suggestion) || value
                                                   .equalsIgnoreCase(suggestion));
        }
        return stream.collect(Collectors.toList());
    }

    @Parser
    @SuppressWarnings("PatternValidation")
    public IDrugForm parseDrugForm(@NotNull CommandContext<CommandSender> context,
                                   @NotNull Queue<String> inputs) throws ParseException {
        final String input = inputs.remove();
        if (input == null) {
            throw new ParseException("No input!", 0);
        } else if (input.indexOf(':') == -1) {
            return this.drugRegistry.formByKey(Utils.internalKey(input)).orElse(null);
        } else {
            return this.drugRegistry.formByKey(Key.key(input)).orElse(null);
        }
    }

    @Parser
    @SuppressWarnings("PatternValidation")
    public IDrugComponent parseDrugComponent(@NotNull CommandContext<CommandSender> context,
                                             @NotNull Queue<String> inputs) throws ParseException {
        final String input = inputs.remove();
        if (input == null) {
            throw new ParseException("No input!", 0);
        } else if (input.indexOf(':') == -1) {
            return this.drugRegistry.componentByKey(Utils.internalKey(input)).orElse(null);
        } else {
            return this.drugRegistry.componentByKey(Key.key(input)).orElse(null);
        }
    }

    @Parser
    @SuppressWarnings("PatternValidation")
    public IDrug parseDrug(@NotNull CommandContext<CommandSender> context,
                           @NotNull Queue<String> inputs) throws ParseException {
        final String input = inputs.remove();
        if (input == null) {
            throw new ParseException("No input!", 0);
        } else if (input.indexOf(':') == -1) {
            return this.drugRegistry.drugByKey(Utils.internalKey(input)).orElse(null);
        } else {
            return this.drugRegistry.drugByKey(Key.key(input)).orElse(null);
        }
    }

    @Hidden
    @ProxiedBy("addictiveexperience")
    @CommandMethod("drugs")
    public void defaultCommand(CommandSender commandSender) {

    }


    @ProxiedBy("drugshop")
    @CommandMethod("drugs shop")
    @CommandPermission("addictiveexperience.shop")
    @CommandDescription("Open the drug shop")
    public void openDrugShop(Player player) {
        this.drugShopUI.openShop(player);
    }


    @CommandMethod("drugs give <item>")
    @CommandDescription("Give a player drug specific items. \n" +
            "Flags: " +
            "amount --> the amount to give" +
            "model-only --> whether the item should be a model or the actual drug"
    )
    @CommandPermission("addictiveexperience.give")
    public void giveItem(Player player,
                         @NotNull
                         @Argument(value = "item", suggestions = "drug-components")
                                 IDrugComponent component,
                         @Flag(value = "form", suggestions = "drug-forms") IDrugForm form,
                         @Flag(value = "amount") @Range(min = "1", max = "64") Integer amt,
                         @Flag(value = "model-only") boolean modelOnly
    ) {
        final ItemStack toGive;
        final int amount = amt == null ? 1 : amt;
        final IDrugForm drugForm = form == null ? this.drugForms.powder() : form;
        if (component instanceof IDrug) {
            if (modelOnly) {
                toGive = drugForm.asItem((IDrug) component);
            } else {
                toGive = this.drugRegistry.itemForDrug((IDrug) component, drugForm);
            }
        } else {
            if (modelOnly) {
                toGive = component.asItem();
            } else {
                toGive = this.drugRegistry.itemForComponent(component);
            }
        }
        toGive.setAmount(amount);
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Component.text("Inventory full!", NamedTextColor.RED));
            return;
        }
        player.getInventory().addItem(toGive);
    }

}
