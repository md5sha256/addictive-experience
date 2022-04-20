package io.github.md5sha256.addictiveexperience.api.drugs;

import org.jetbrains.annotations.NotNull;

public interface IDrug extends IDrugComponent {

    @NotNull String permission();

    @NotNull DrugMeta defaultMeta();

}
