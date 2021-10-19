package com.github.md5sha256.addictiveexperience.api.drugs;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface PassiveEffect {

    void onPresentInInventory(@NotNull InventoryHolder inventoryHolder);
    
    void onRemovalFromInventory(@NotNull InventoryHolder inventoryHolder);
    
    void onPresentInEquipmentSlot(@NotNull EquipmentSlot equipmentSlot);
    
    void onRemovalFromEquipmentSlot(@NotNull EquipmentSlot equipmentSlot);
    

}
