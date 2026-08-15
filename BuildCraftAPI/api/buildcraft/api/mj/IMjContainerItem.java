package buildcraft.api.mj;

import net.minecraft.world.item.ItemStack;

public interface IMjContainerItem {
    long receivePower(ItemStack container, long maxReceive, boolean simulate);

    long extractPower(ItemStack container, long maxExtract, boolean simulate);

    long getPowerStored(ItemStack container);

    long getMaxPowerStored(ItemStack container);
}
