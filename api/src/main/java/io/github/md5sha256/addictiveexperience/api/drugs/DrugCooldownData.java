package io.github.md5sha256.addictiveexperience.api.drugs;

import io.github.md5sha256.addictiveexperience.api.drugs.impl.DrugCooldownDataImpl;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DrugCooldownData {

    static @NotNull DrugCooldownData create() {
        return new DrugCooldownDataImpl();
    }

    void clear();

    void clear(@NotNull UUID player);

    void clear(@NotNull UUID player, @NotNull IDrug drug);

    void clear(@NotNull UUID player, @NotNull IDrugForm drugForm);

    boolean isBlocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm);

    void setBlocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm);

    void setUnblocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm);
}
