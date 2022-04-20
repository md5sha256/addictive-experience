package io.github.md5sha256.addictiveexperience.api.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SimilarLike<T> {

    boolean isSimilar(@NotNull T other);

}
