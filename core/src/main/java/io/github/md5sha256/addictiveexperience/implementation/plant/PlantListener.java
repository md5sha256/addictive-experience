package io.github.md5sha256.addictiveexperience.implementation.plant;

import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantMeta;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import io.github.md5sha256.addictiveexperience.api.drugs.IDrugComponent;
import io.github.md5sha256.addictiveexperience.api.drugs.IPlantHandler;
import com.github.md5sha256.spigotutils.DeregisterableListener;
import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Handles plant placement, interaction and breakages.'
 * FIXME: Bukkit call's the interact event twice on the {@link org.bukkit.block.data.Bisected.Half#BOTTOM},
 * FIXME: fix the duplicate method calls
 */
@Singleton
public final class PlantListener implements DeregisterableListener {

    private final Random random = new Random();

    private final Map<BlockBreakEvent, ItemStack> dropMap = new HashMap<>();
    private final Plugin plugin;
    @Inject
    private IPlantHandler plantHandler;
    @Inject
    private BukkitScheduler scheduler;
    @Inject
    private DrugRegistry drugRegistry;

    @Inject
    PlantListener(@NotNull Plugin plugin, @NotNull Server server) {
        this.plugin = plugin;
        server.getPluginManager().registerEvents(this, plugin);
    }

    private @NotNull Optional<DrugPlantData> parsePlantData(@NotNull Block block) {
        final BlockState blockState = PaperLib.getBlockState(block, false).getState();
        final BlockData blockData = blockState.getBlockData();
        if (blockData instanceof final Bisected bisected) {
            final BlockPosition top;
            final BlockPosition bottom;
            switch (bisected.getHalf()) {
                case BOTTOM -> {
                    top = new BlockPosition(block.getRelative(BlockFace.UP));
                    bottom = new BlockPosition(block);
                }
                case TOP -> {
                    top = new BlockPosition(block);
                    bottom = new BlockPosition(block.getRelative(BlockFace.DOWN));
                }
                default -> throw new IllegalStateException("Unknown half: " + bisected.getHalf());
            }
            final Optional<DrugPlantData> plantDataTop = this.plantHandler.plantData(top);
            if (plantDataTop.isPresent()) {
                return plantDataTop;
            }
            return this.plantHandler.plantData(bottom);
        } else {
            return this.plantHandler.plantData(new BlockPosition(block));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        final Optional<IDrugComponent> optionalComponent = this.drugRegistry
                .componentFromItem(event.getItemInHand());
        if (optionalComponent.isEmpty()) {
            return;
        }
        final IDrugComponent component = optionalComponent.get();
        final Optional<DrugPlantMeta> optionalPlantMeta = this.drugRegistry
                .metaData(component, DrugPlantMeta.KEY);
        if (optionalPlantMeta.isEmpty()) {
            return;
        }
        final DrugPlantMeta plantMeta = optionalPlantMeta.get();
        final BlockPosition blockPosition = new BlockPosition(event.getBlock());
        final DrugPlantData plantData = DrugPlantData.builder()
                                                     .meta(plantMeta)
                                                     .position(blockPosition)
                                                     .build();
        this.plantHandler.addEntry(blockPosition, plantData);
        event.getPlayer().sendMessage(Component
                                              .text("You have placed a drug plant! Plant: " + component
                                                      .displayName()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        final Optional<DrugPlantData> optionalPlantData = parsePlantData(event.getBlock());
        if (optionalPlantData.isEmpty()) {
            return;
        }
        final DrugPlantData data = optionalPlantData.get();
        final DrugPlantMeta meta = data.meta();
        final Optional<IDrugComponent> optionalSeed = meta.seed();

        event.setDropItems(false);
        event.setExpToDrop(0);
        event.getPlayer().sendMessage(Component.text("You have broken a drug plant. Result: " + meta
                .result().displayName()));

        if (optionalSeed.isEmpty()) {
            return;
        }
        final IDrugComponent component = optionalSeed.get();
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
        final Optional<DrugPlantData> optionalData = parsePlantData(clicked);
        if (optionalData.isEmpty()) {
            return;
        }
        final DrugPlantData data = optionalData.get();
        if (data.remainingMillis() != 0) {
            event.getPlayer()
                 .sendMessage(Component.text("This plant isn't ready to be harvested yet!"));
            return;
        }
        handleHarvest(clicked, data);
        event.getPlayer().sendMessage(Component
                                              .text("You have harvested a drug plant. Result: " + data
                                                      .meta().result().displayName()));
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
        final ItemStack itemDrug = meta.result().asItem(amountHarvest);
        world.dropItemNaturally(location, itemDrug);
        if (dropSeeds) {
            final IDrugComponent component = optionalSeed.get();
            final ItemStack itemSeed = component.asItem(amountSeed);
            world.dropItemNaturally(location, itemSeed);
        }
    }

}
