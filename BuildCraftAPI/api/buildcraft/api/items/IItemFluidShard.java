package buildcraft.api.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IItemFluidShard {
    void addFluidDrops(NonNullList<ItemStack> toDrop, @Nullable FluidStack fluid);
}
