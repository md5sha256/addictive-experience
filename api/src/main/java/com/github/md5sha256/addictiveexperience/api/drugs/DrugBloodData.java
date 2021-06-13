package com.github.md5sha256.addictiveexperience.api.drugs;

import com.github.md5sha256.addictiveexperience.api.drugs.impl.DrugBloodDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DrugBloodData {

    static DrugBloodData create(@NotNull UUID player) {
        return new DrugBloodDataImpl(player);
    }

    UUID player();

    int getLevel(@NotNull IDrug drug);

    void setLevel(@NotNull IDrug drug, int level);

    void incrementLevel(@NotNull IDrug drug, int toChange);

    void reset(@NotNull IDrug drug);

    void reset();

}
