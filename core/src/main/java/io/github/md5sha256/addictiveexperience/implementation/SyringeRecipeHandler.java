package io.github.md5sha256.addictiveexperience.implementation;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.implementation.forms.FormSyringe;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SyringeRecipeHandler {

    private final DrugRegistry registry;
    private final FormSyringe syringe;

    @Inject
    SyringeRecipeHandler(@NotNull DrugRegistry drugRegistry,
                         @NotNull FormSyringe syringe) {
        this.registry = drugRegistry;
        this.syringe = syringe;
    }

    public void registerRecipes() {
        this.registry.drugs().forEach(this.syringe::registerFilledSyringeRecipe);
    }

}
