package com.github.md5sha256.addictiveexperience.util;

import net.kyori.adventure.key.Key;
import org.bukkit.ChatColor;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class Utils {

    public static String legacyColorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String capitalise(final String string) {
        if (string.length() <= 1) {
            return string.toUpperCase();
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String titleCase(final String string, final String delimiter) {
        final StringJoiner joiner = new StringJoiner(delimiter);
        for (final String s : string.split(delimiter)) {
            joiner.add(capitalise(s));
        }
        return joiner.toString();
    }

    @SuppressWarnings("PatternValidation")
    public static Key internalKey(@NotNull @Pattern("[a-z0-9_\\-./]+") String value) {
        return Key.key("drugfun", value);
    }

}
