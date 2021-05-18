package com.github.md5sha256.addictiveexperience.api.util;

import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DataKeyImpl<T> implements DataKey<T> {

    private final String name;
    private final TypeToken<T> type;

    public DataKeyImpl(@NotNull String name, @NotNull TypeToken<T> type) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public @NotNull String name() {
        return this.name;
    }

    @Override
    public @NotNull TypeToken<T> type() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataKeyImpl<?> dataKey = (DataKeyImpl<?>) o;

        if (!name.equals(dataKey.name)) return false;
        return type.equals(dataKey.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DataKeyImpl{" +
                "name='" + name + '\'' +
                ", type=" + type.getType().getTypeName() +
                '}';
    }
}
