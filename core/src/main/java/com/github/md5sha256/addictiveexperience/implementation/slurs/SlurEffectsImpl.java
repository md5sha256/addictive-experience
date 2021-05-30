package com.github.md5sha256.addictiveexperience.implementation.slurs;

import com.github.md5sha256.addictiveexperience.api.slur.ISlurEffect;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffects;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

@Singleton
public final class SlurEffectsImpl implements SlurEffects {

    private static final Pattern PATTERN_PLAYER = Pattern.compile("\\{PLAYER}");
    private static final Pattern PATTERN_SPACE = Pattern.compile("\\h");
    private static final Pattern PATTERN_CUSTOM_VOWELS = Pattern
            .compile("[AEIOY]", Pattern.CASE_INSENSITIVE);

    private final ISlurEffect ellipses = SlurEffectsImpl::formatEllipses;
    private final ISlurEffect exclamation = SlurEffectsImpl::formatExclamation;
    private final ISlurEffect question = SlurEffectsImpl::formatQuestion;
    private final ISlurEffect noVowels = SlurEffectsImpl::stripVowels;

    SlurEffectsImpl() {
    }

    private static @NotNull Component formatEllipses(@NotNull Player player,
                                                     @NotNull Component message) {
        return message.replaceText(builder -> builder.match(PATTERN_SPACE).replacement("..."));
    }

    private static @NotNull Component stripVowels(@NotNull Player player,
                                                  @NotNull Component message) {
        return message.replaceText(builder -> builder.match(PATTERN_CUSTOM_VOWELS).replacement(""));
    }

    private static @NotNull Component formatExclamation(@NotNull Player player,
                                                        @NotNull Component message) {
        return message.replaceText(builder -> builder
                .match(PATTERN_SPACE)
                .replacement(component -> component
                        .content(component.content().toUpperCase(Locale.ROOT) + "! ")));
    }

    private static @NotNull Component formatQuestion(@NotNull Player player,
                                                     @NotNull Component message) {
        return message.replaceText(
                builder -> builder.match(PATTERN_SPACE).replacement(
                        component -> {
                            final String content = component.content();
                            final int[] codePoints = content.toLowerCase(Locale.ROOT).codePoints()
                                                            .toArray();
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

    @Override
    public @NotNull ISlurEffect ellipses() {
        return this.ellipses;
    }

    @Override
    public @NotNull ISlurEffect exclamation() {
        return this.exclamation;
    }

    @Override
    public @NotNull ISlurEffect question() {
        return this.question;
    }

    @Override
    public @NotNull ISlurEffect noVowels() {
        return this.noVowels;
    }

}
