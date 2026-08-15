package buildcraft.api.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import buildcraft.lib.registry.RegistryObject;

public class FluidItemDrops {
    public static RegistryObject<? extends IItemFluidShard> item;

    public static void addFluidDrops(NonNullList<ItemStack> toDrop, FluidStack... fluids) {
        if (item != null) {
            for (FluidStack fluid : fluids) {
                item.get().addFluidDrops(toDrop, fluid);
            }
        }
    }

    public static void addFluidDrops(NonNullList<ItemStack> toDrop, IFluidTank... tanks) {
        if (item != null) {
            for (IFluidTank tank : tanks) {
                item.get().addFluidDrops(toDrop, tank.getFluid());
            }
        }
    }
}
