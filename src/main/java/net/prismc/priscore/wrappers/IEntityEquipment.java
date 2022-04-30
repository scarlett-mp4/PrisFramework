package net.prismc.priscore.wrappers;

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import org.bukkit.inventory.ItemStack;

public interface IEntityEquipment {

    void setItem(ItemSlot slot, ItemStack stack);

}
