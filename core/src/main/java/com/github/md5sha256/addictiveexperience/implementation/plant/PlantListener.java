package com.github.md5sha256.addictiveexperience.implementation.plant;

import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import com.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import com.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import com.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import com.github.md5sha256.spigotutils.DeregisterableListener;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Singleton
public final class PlantListener implements DeregisterableListener {

    private final Random random = new Random();

    private final Map<BlockBreakEvent, ItemStack> dropMap = new HashMap<>();
    @Inject
    private IPlantHandler plantHandler;
    @Inject
    private BukkitScheduler scheduler;
    @Inject
    private Plugin plugin;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        final BlockPosition broken = new BlockPosition(event.getBlock());
        final Optional<DrugPlantData> optionalPlantData = this.plantHandler.plantData(broken);
        if (!optionalPlantData.isPresent()) {
            return;
        }
        final DrugPlantData data = optionalPlantData.get();
        final DrugPlantMeta meta = data.meta();
        final Optional<IDrugComponent> optionalSeed = meta.seed();
        if (!optionalSeed.isPresent()) {
            return;
        }
        final IDrugComponent component = optionalSeed.get();
        event.setDropItems(false);
        event.setExpToDrop(0);
        this.dropMap.put(event, component.asItem(1));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleBlockDrops(@NotNull BlockBreakEvent event) {
        final ItemStack itemStack = this.dropMap.remove(event);
        if (itemStack == null) {
            return;
        }
        final World world = event.getBlock().getWorld();
        final Location location = event.getBlock().getLocation();
        this.scheduler
                .runTaskLater(this.plugin, () -> world.dropItemNaturally(location, itemStack), 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHarvest(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Block clicked = event.getClickedBlock();
        if (clicked == null) {
            return;
        }
        final BlockPosition position = new BlockPosition(clicked);
        final Optional<DrugPlantData> optionalData = this.plantHandler.updatePosition(position);
        if (!optionalData.isPresent()) {
            return;
        }
        final DrugPlantData data = optionalData.get();
        handleHarvest(clicked, data);
    }

    private void handleHarvest(@NotNull Block block, @NotNull DrugPlantData data) {
        final DrugPlantMeta meta = data.meta();

        final double probabilityHarvest = meta.harvestSuccessProbability();
        final int amountHarvest = meta.harvestAmount();
        final boolean harvest = this.random.nextDouble() >= probabilityHarvest;
        if (!harvest) {
            return;
        }
        final Optional<IDrugComponent> optionalSeed = meta.seed();
        final double probabilitySeedDrop = meta.seedDropProbability();
        final int amountSeed = meta.seedDropAmount();
        final boolean dropSeeds;
        if (optionalSeed.isPresent()) {
            dropSeeds = this.random.nextDouble() >= probabilitySeedDrop;
        } else {
            dropSeeds = false;
        }

        final World world = block.getWorld();
        final Location location = block.getLocation();
        final ItemStack itemDrug = meta.drug().asItem(amountHarvest);
        world.dropItemNaturally(location, itemDrug);
        if (dropSeeds) {
            final IDrugComponent component = optionalSeed.get();
            final ItemStack itemSeed = component.asItem(amountSeed);
            world.dropItemNaturally(location, itemSeed);
        }
    }

}
