package com.github.md5sha256.addictiveexperience.configuration;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface DrugConfiguration {

    @NotNull Set<IDrug> drugs();

    void drugs(@NotNull Collection<IDrug> drugs);

    @NotNull Map<IDrug, @NotNull DrugMeta> drugMeta();

    void drugMeta(@NotNull Map<IDrug, @NotNull DrugMeta> metaMap);

}
