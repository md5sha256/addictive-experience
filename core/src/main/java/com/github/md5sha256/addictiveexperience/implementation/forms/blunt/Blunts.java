package com.github.md5sha256.addictiveexperience.implementation.forms.blunt;

import com.github.md5sha256.addictiveexperience.api.forms.BluntState;
import com.github.md5sha256.addictiveexperience.api.forms.FormBlunt;
import com.github.md5sha256.addictiveexperience.api.forms.IBlunts;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class Blunts implements IBlunts {

    private final BluntLit lit;
    private final BluntUnlit unlit;

    Blunts(@NotNull BluntLit lit, @NotNull BluntUnlit unlit) {
        this.lit = lit;
        this.unlit = unlit;
    }

    @Override
    public @NotNull FormBlunt lit() {
        return this.lit;
    }

    @Override
    public @NotNull FormBlunt unlit() {
        return this.unlit;
    }

    @Override
    public @NotNull FormBlunt of(@NotNull BluntState state) {
        switch (state) {
            case LIT:
                return this.lit;
            case UNLIT:
                return this.unlit;
            default:
                throw new IllegalArgumentException("Unknown BluntState: " + state);
        }
    }

}
