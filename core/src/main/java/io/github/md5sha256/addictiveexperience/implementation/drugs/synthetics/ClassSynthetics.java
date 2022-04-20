package io.github.md5sha256.addictiveexperience.implementation.drugs.synthetics;

import io.github.md5sha256.addictiveexperience.implementation.DrugClass;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class ClassSynthetics implements DrugClass {

    private final Key key = Key.key("addictive-experience", "synthetics");
    
    @Override
    public @NotNull String displayName() {
        return "Synthetics";
    }

    @Override
    public @NonNull Key key() {
        return this.key;
    }
}
