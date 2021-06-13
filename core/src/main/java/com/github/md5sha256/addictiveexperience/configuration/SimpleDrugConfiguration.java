package com.github.md5sha256.addictiveexperience.configuration;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleDrugConfiguration implements DrugConfiguration {

    private final Map<IDrug, DrugMeta> drugs = new HashMap<>();

    @Override
    public @NotNull Set<IDrug> drugs() {
        return new HashSet<>(this.drugs.keySet());
    }

    @Override
    public void drugs(@NotNull final Collection<IDrug> drugs) {

    }

    @Override
    public @NotNull Map<IDrug, @NotNull DrugMeta> drugMeta() {
        return Collections.emptyMap();
    }

    @Override
    public void drugMeta(@NotNull final Map<IDrug, @NotNull DrugMeta> metaMap) {

    }
}
