package com.github.md5sha256.addictiveexperience.api.forms;

import org.jetbrains.annotations.NotNull;

public interface IBlunts {

    @NotNull FormBlunt lit();

    @NotNull FormBlunt unlit();

    @NotNull FormBlunt of(@NotNull BluntState state);

}
