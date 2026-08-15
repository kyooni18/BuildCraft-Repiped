package buildcraft.api.compat.capability;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

/** Legacy Forge capability names mapped onto their NeoForge 1.21.1 equivalents. */
public final class ForgeCapabilities {
    public static final Capability<IItemHandler> ITEM_HANDLER = new Capability<>(
            IItemHandler.class,
            Capabilities.ItemHandler.BLOCK,
            Capabilities.ItemHandler.ENTITY_AUTOMATION,
            Capabilities.ItemHandler.ITEM
    );
    public static final Capability<IFluidHandler> FLUID_HANDLER = new Capability<>(
            IFluidHandler.class,
            Capabilities.FluidHandler.BLOCK,
            Capabilities.FluidHandler.ENTITY,
            Capabilities.FluidHandler.ITEM
    );
    public static final Capability<IFluidHandlerItem> FLUID_HANDLER_ITEM = new Capability<>(
            IFluidHandlerItem.class,
            null,
            null,
            Capabilities.FluidHandler.ITEM
    );
    public static final Capability<IEnergyStorage> ENERGY = new Capability<>(
            IEnergyStorage.class,
            Capabilities.EnergyStorage.BLOCK,
            Capabilities.EnergyStorage.ENTITY,
            Capabilities.EnergyStorage.ITEM
    );

    private ForgeCapabilities() {}
}
