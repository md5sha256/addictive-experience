package com.github.md5sha256.addictiveexperience;

import com.github.md5sha256.addictiveexperience.api.AddictiveExperienceApi;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import com.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import com.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import com.github.md5sha256.addictiveexperience.configuration.DrugFunConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AddictiveExperience extends JavaPlugin implements AddictiveExperienceApi {

    private DrugRegistry drugRegistry;
    private IDrugForms drugForms;
    private SlurEffectState slurEffectState;
    private DrugHandler drugHandler;
    private DrugFunConfiguration drugFunConfiguration;

    @Override
    public @NotNull DrugRegistry drugRegistry() {
        return this.drugRegistry;
    }

    @Override
    public @NotNull IDrugForms drugForms() {
        return this.drugForms;
    }

    @Override
    public @NotNull SlurEffectState slurEffectState() {
        return this.slurEffectState;
    }

    @Override
    public @NotNull DrugHandler drugHandler() {
        return this.drugHandler;
    }

    public @NotNull DrugFunConfiguration drugFunConfiguration() {
        return this.drugFunConfiguration;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
