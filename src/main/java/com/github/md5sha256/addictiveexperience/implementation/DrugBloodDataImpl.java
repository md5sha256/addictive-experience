package com.github.md5sha256.addictiveexperience.implementation;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugBloodData;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DrugBloodDataImpl implements DrugBloodData {

    private final UUID player;

    private final Map<IDrug, Integer> levelMap = new HashMap<>();

    public DrugBloodDataImpl(@NotNull UUID player) {
        this.player = player;
    }

    @Override
    public UUID player() {
        return this.player;
    }

    @Override
    public int getLevel(@NotNull IDrug drug) {
        return this.levelMap.getOrDefault(drug, 0);
    }

    @Override
    public void setLevel(@NotNull IDrug drug, int level) {
        if (level == 0) {
            this.levelMap.remove(drug);
        } else {
            this.levelMap.put(drug, level);
        }
    }

    @Override
    public void incrementLevel(@NotNull IDrug drug, int toChange) {
        final int existing = this.levelMap.getOrDefault(drug, 0);
        setLevel(drug, existing + toChange);
    }

    @Override
    public void reset(@NotNull IDrug drug) {
        this.levelMap.remove(drug);
    }

    @Override
    public void reset() {
        this.levelMap.clear();
    }
}
