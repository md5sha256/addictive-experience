package com.github.md5sha256.addictiveexperience.util;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdventureUtils {

    public static List<String> toLegacy(@NotNull List<Component> components) {
        final List<String> legacy = new ArrayList<>(components.size());
        for (Component c : components) {
            legacy.add(LegacyComponentSerializer.legacySection().serialize(c));
        }
        return legacy;
    }

    public static String toLegacy(@NotNull Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static List<Component> fromLegacy(@NotNull List<String> legacy) {
        final List<Component> components = new ArrayList<>(legacy.size());
        for (String s : legacy) {
            components.add(LegacyComponentSerializer.legacySection().deserialize(s));
        }
        return components;
    }

    public static Component fromLegacy(@NotNull String legacy) {
        return LegacyComponentSerializer.legacySection().deserialize(legacy);
    }

    public static Component setDisplayName(@NotNull Player player) {
        if (PaperLib.isPaper()) {
            return player.displayName();
        } else {
            return fromLegacy(player.getDisplayName());
        }
    }

    public static void setDisplayName(@NotNull Player player, @NotNull Component displayName) {
        if (PaperLib.isPaper()) {
            player.displayName(displayName);
        } else {
            player.setDisplayName(toLegacy(displayName));
        }
    }


    public static Component setDisplayName(@NotNull ItemMeta meta) {
        if (PaperLib.isPaper()) {
            return meta.displayName();
        } else {
            return fromLegacy(meta.getDisplayName());
        }
    }

    public static void setDisplayName(@NotNull ItemMeta meta, @NotNull Component displayName) {
        if (PaperLib.isPaper()) {
            meta.displayName(displayName);
        } else {
            meta.setDisplayName(toLegacy(displayName));
        }
    }

    public static void getLine(@NotNull Sign sign, int index, @NotNull Component line) {
        if (PaperLib.isPaper()) {
            sign.line(index, line);
        } else {
            sign.setLine(index, toLegacy(line));
        }
    }


    public static Component getLine(@NotNull Sign sign, int index) {
        if (PaperLib.isPaper()) {
            return sign.line(index);
        } else {
            return fromLegacy(sign.getLine(index));
        }
    }

    public static List<Component> getLines(@NotNull Sign sign) {
        if (PaperLib.isPaper()) {
            return sign.lines();
        } else {
            final List<Component> lines = new ArrayList<>(3);
            for (String legacy : sign.getLines()) {
                lines.add(fromLegacy(legacy));
            }
            return lines;
        }
    }

    public static void setLore(@NotNull ItemMeta meta, @NotNull List<Component> lore) {
        if (PaperLib.isPaper()) {
            meta.lore(lore);
        } else {
            meta.setLore(toLegacy(lore));
        }
    }

}
