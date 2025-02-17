package io.github.md5sha256.addictiveexperience;

import com.google.inject.Key;
import com.google.inject.name.Names;
import io.github.md5sha256.addictiveexperience.api.AddictiveExperience;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import io.github.md5sha256.addictiveexperience.api.effect.IEffectHandler;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForms;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import io.github.md5sha256.addictiveexperience.configuration.AddictiveExperienceConfiguration;
import io.github.md5sha256.addictiveexperience.implementation.SyringeRecipeHandler;
import io.github.md5sha256.addictiveexperience.implementation.plant.PlantHandlerImpl;
import io.github.md5sha256.addictiveexperience.module.AddictiveExperienceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class AddictiveExperiencePlugin extends JavaPlugin implements AddictiveExperience {

    private Injector injector;
    private DrugRegistry drugRegistry;
    private IPlantHandler plantHandler;
    private IDrugForms drugForms;
    private SlurEffectState slurEffectState;
    private DrugHandler drugHandler;
    private AddictiveExperienceConfiguration addictiveExperienceConfiguration;
    private IEffectHandler effectHandler;

    @Override
    public @NotNull DrugRegistry drugRegistry() {
        return this.drugRegistry;
    }

    @Override
    public @NotNull IPlantHandler plantHandler() {
        return this.plantHandler;
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

    public @NotNull AddictiveExperienceConfiguration addictiveExperienceConfiguration() {
        return this.addictiveExperienceConfiguration;
    }

    private void initDataFolder() {
        if (!getDataFolder().isDirectory()) {
            getDataFolder().mkdirs();
        }
    }

    private void initializeInjector() {
        getLogger().info("Initialising the injector...");
        final Injector injector = Guice
                .createInjector(Stage.PRODUCTION, new AddictiveExperienceModule(this, this));
        this.drugForms = injector.getInstance(IDrugForms.class);
        this.slurEffectState = injector.getInstance(SlurEffectState.class);
        this.drugHandler = injector.getInstance(DrugHandler.class);
        // Missing bindings/implementations
        this.addictiveExperienceConfiguration = injector.getInstance(
                AddictiveExperienceConfiguration.class);
        this.drugRegistry = injector.getInstance(DrugRegistry.class);
        this.plantHandler = injector.getInstance(IPlantHandler.class);
        this.effectHandler = injector.getInstance(IEffectHandler.class);
        SyringeRecipeHandler recipeHandler = injector.getInstance(SyringeRecipeHandler.class);
        recipeHandler.registerRecipes();
        getServer().getPluginManager().registerEvents(this.plantHandler, this);
        this.injector = injector;
    }

    private void shutdownTasks() {
        this.plantHandler.shutdown();
        this.effectHandler.shutdown();
    }

    private void shutdownInjector() {
        ExecutorService executorService
                = this.injector.getInstance(Key.get(ExecutorService.class,
                Names.named("database")));
        try {
            executorService.close();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        shutdownTasks();
        shutdownInjector();
        getLogger().info("Plugin disabled!");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        initDataFolder();
        try {
            initializeInjector();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        registerRecipes(getServer());
        getLogger().info("Plugin Enabled!");
    }

    public void registerRecipes(@NotNull Server server) {
        for (IDrugComponent component : this.drugRegistry.components()) {
            component.recipe().ifPresent(server::addRecipe);
        }
    }
}
