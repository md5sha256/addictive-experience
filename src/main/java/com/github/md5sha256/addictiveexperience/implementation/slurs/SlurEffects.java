package com.github.md5sha256.addictiveexperience.implementation.slurs;

import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

public class SlurEffects {

    private static final Pattern PATTERN_PLAYER = Pattern.compile("\\{PLAYER}");
    private static final Pattern PATTERN_SPACE = Pattern.compile("\\h");
    public static final ISlurEffect ELLIPSES = SlurEffects::formatEllipses;
    public static final ISlurEffect EXCLAMATION = SlurEffects::formatExclamation;
    public static final ISlurEffect QUESTION = SlurEffects::formatQuestion;
    private static final Pattern PATTERN_CUSTOM_VOWELS = Pattern.compile("[AEIOY]", Pattern.CASE_INSENSITIVE);
    public static final ISlurEffect NO_VOWELS = SlurEffects::stripVowels;

    private static @NotNull Component formatEllipses(@NotNull Player player, @NotNull Component message) {
        return message.replaceText(builder -> builder.match(PATTERN_SPACE).replacement("..."));
    }

    private static @NotNull Component stripVowels(@NotNull Player player, @NotNull Component message) {
        return message.replaceText(builder -> builder.match(PATTERN_CUSTOM_VOWELS).replacement(""));
    }

    private static @NotNull Component formatExclamation(@NotNull Player player, @NotNull Component message) {
        return message.replaceText(builder -> builder
                .match(PATTERN_SPACE)
                .replacement(component -> component.content(component.content().toUpperCase(Locale.ROOT) + "! ")));
    }

    private static @NotNull Component formatQuestion(@NotNull Player player, @NotNull Component message) {
        return message.replaceText(
                builder -> builder.match(PATTERN_SPACE).replacement(
                        component -> {
                            final String content = component.content();
                            final int[] codePoints = content.toLowerCase(Locale.ROOT).codePoints().toArray();
                            final StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < codePoints.length; i++) {
                                final int codePoint;
                                if (i % 2 == 0) {
                                    codePoint = Character.toUpperCase(codePoints[i]);
                                } else {
                                    codePoint = codePoints[i];
                                }
                                stringBuilder.appendCodePoint(codePoint);
                            }
                            return component.content(stringBuilder.toString());
                        }
                )
        );
    }

}
