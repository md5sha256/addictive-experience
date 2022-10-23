package io.github.md5sha256.addictiveexperience.implementation.forms.blunt;

import io.github.md5sha256.addictiveexperience.api.forms.BluntState;
import io.github.md5sha256.addictiveexperience.api.forms.FormBlunt;
import io.github.md5sha256.addictiveexperience.api.forms.IBlunts;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class Blunts implements IBlunts {

    private final BluntLit lit;
    private final BluntUnlit unlit;

    @Inject
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
        return switch (state) {
            case LIT -> this.lit;
            case UNLIT -> this.unlit;
            default -> throw new IllegalArgumentException("Unknown BluntState: " + state);
        };
    }

}
