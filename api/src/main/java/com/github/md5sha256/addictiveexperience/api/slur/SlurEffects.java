package com.github.md5sha256.addictiveexperience.api.slur;

import org.jetbrains.annotations.NotNull;

public interface SlurEffects {

    @NotNull ISlurEffect ellipses();

    @NotNull ISlurEffect exclamation();

    @NotNull ISlurEffect question();

    @NotNull ISlurEffect noVowels();

}
