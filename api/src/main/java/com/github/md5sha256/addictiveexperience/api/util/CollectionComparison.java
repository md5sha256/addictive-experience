package com.github.md5sha256.addictiveexperience.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class CollectionComparison {

    public static <T> boolean haveIdenticalElements(@NotNull Set<T> set1, @NotNull Set<T> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return true;
        } else if (set1.size() != set2.size()) {
            return false;
        }
        // If they are of the same size and set1 contains all of set2, they have identical elements.
        return set1.containsAll(set2);
    }

}
