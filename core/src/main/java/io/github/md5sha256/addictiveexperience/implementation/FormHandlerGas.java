package io.github.md5sha256.addictiveexperience.implementation;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugHandler;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugItemData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.effect.IEffectHandler;
import io.github.md5sha256.addictiveexperience.api.forms.IDrugForm;
import io.github.md5sha256.addictiveexperience.api.slur.SlurEffectState;
import io.github.md5sha256.addictiveexperience.implementation.forms.DrugForms;
import jakarta.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FormHandlerGas extends AbstractFormHandler implements Listener {

    private final IDrugForm form;


    @Inject
    FormHandlerGas(@NotNull Plugin plugin,
                             @NotNull DrugRegistry drugRegistry,
                             @NotNull DrugHandler drugHandler,
                             @NotNull SlurEffectState effectState,
                             @NotNull IEffectHandler effectHandler,
                             @NotNull DrugForms forms) {
        super(plugin, drugRegistry, drugHandler, effectState, effectHandler);
        this.form = forms.gas();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEntityEvent event) {
        final Entity clicked = event.getRightClicked();
        if (!(clicked instanceof LivingEntity livingEntity)) {
            return;
        }
        final Player player = event.getPlayer();
        final ItemStack used = player.getInventory().getItem(event.getHand());
        final Optional<DrugItemData> optionalData = this.drugRegistry.dataFor(used);
        if (optionalData.isEmpty()
                || !optionalData.get().form().equals(this.form)
                || !checkPermissions(player, optionalData.get())) {
            return;
        }

        handleDrugUse(livingEntity, used, optionalData.get());
        player.getInventory()
                .setItem(event.getHand(), used.getAmount() == 1 ? null : used.subtract(1));
        clicked.sendMessage(Component.text("You have been knocked out from a chloroform attack!",
                NamedTextColor.RED));
    }

    @Override
    protected void playSounds(@NotNull Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_FOX_SNIFF, 1.0f, 0.6f);
    }

    @Override
    protected void sendMessageOnItemUse(@NotNull Player player, @NotNull DrugItemData used) {
        player.sendMessage(Component.text("You have used chloroform!", NamedTextColor.RED));
    }
}
