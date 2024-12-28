package io.github.md5sha256.addictiveexperience.fixtures;

import io.github.md5sha256.addictiveexperience.api.drugs.IDrug;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.jetbrains.annotations.NotNull;

public final class DummyDrugBuilder {

    private final ItemFactory factory;

    private String permission;
    private String name;
    private Material material;
    private IDrugForm drugForm;

    public DummyDrugBuilder(@NotNull ItemFactory itemFactory) {
        this.factory = itemFactory;
    }

    public DummyDrugBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public DummyDrugBuilder name(@NotNull String name) {
        this.name = name;
        return this;
    }

    public DummyDrugBuilder material(@NotNull Material material) {
        this.material = material;
        return this;
    }

    public DummyDrugBuilder drugForm(@NotNull IDrugForm drugForm) {
        this.drugForm = drugForm;
        return this;
    }

    public IDrug build() {
        if (permission == null) {
            this.permission = "";
        }
        final Key key = Key.key("dummy", this.name);
        return new DummyDrugImpl(factory, key, name, material, permission, drugForm);
    }
}
