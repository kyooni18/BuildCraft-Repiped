package buildcraft.lib.misc;

import buildcraft.api.core.IFluidHandlerAdv;
import buildcraft.api.inventory.IItemTransactor;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import buildcraft.api.compat.capability.*;
import buildcraft.api.compat.LazyOptional;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapUtil {
    // @CapabilityInject(IItemTransactor.class)
    private static Capability<IItemTransactor> capTransactor = CapabilityManager.get(new CapabilityToken<>() {
    });
    private static Capability<IFluidHandlerAdv> capFluidHandlerAdv = CapabilityManager.get(new CapabilityToken<>() {
    });
    @Nonnull
    public static final Capability<IItemHandler> CAP_ITEMS = getCapNonNull(ForgeCapabilities.ITEM_HANDLER, IItemHandler.class);

    @Nonnull
    public static final Capability<IFluidHandler> CAP_FLUIDS = getCapNonNull(ForgeCapabilities.FLUID_HANDLER, IFluidHandler.class);
    @Nonnull
    public static final Capability<IItemTransactor> CAP_ITEM_TRANSACTOR = getCapNonNull(capTransactor, IItemTransactor.class);
    public static final Capability<IFluidHandlerAdv> CAP_FLUID_HANDLER_ADV = getCapNonNull(capFluidHandlerAdv, IFluidHandlerAdv.class);

    // Calen: called in BCLib
    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent evt) {
        // Capability type objects no longer need class registration in NeoForge. Instead expose BuildCraft's
        // compatibility providers through the real block/entity capability registry.
        LegacyCapabilityRegistry.registerNeoForgeProviders(evt);
    }

    @Nonnull
    private static <T> Capability<T> getCapNonNull(Capability<T> cap, Class<T> clazz) {
        if (cap == null) {
            throw new NullPointerException("The capability " + clazz + " was null!");
        }
        return cap;
    }

//    private static <T> void registerAbstractCapability(Class<T> clazz) {
//        // By default storing and creating are illegal operations, as we don't necessarily have good default impl's
//        IStorage<T> ourStorage = new IStorage<T>() {
//            @Override
//            public Tag writeNBT(Capability<T> capability, T instance, Direction side) {
//                throw new IllegalStateException("You must provide your own implementations of " + clazz);
//            }
//
//            @Override
//            public void readNBT(Capability<T> capability, T instance, Direction side, Tag nbt) {
//                throw new IllegalStateException("You must provide your own implementations of " + clazz);
//            }
//        };
//        Callable<T> factory = () -> {
//            throw new IllegalStateException("You must provide your own instances of " + clazz);
//        };
//        CapabilityManager.INSTANCE.register(clazz, ourStorage, factory);
//    }

    /** Attempts to fetch the given capability from the given provider, or returns null if either of those two are
     * null. */
    @Nullable
    public static <T> LazyOptional<T> getCapability(Object provider, Capability<T> capability, Direction facing) {
        if (provider == null || capability == null) {
            return LazyOptional.empty();
        }
        if (provider instanceof ICapabilityProvider legacy) {
            LazyOptional<T> value = legacy.getCapability(capability, facing);
            if (value.isPresent()) return value;
        }
        if (provider instanceof BlockEntity blockEntity && blockEntity.getLevel() != null && capability.block() != null) {
            T value = blockEntity.getLevel().getCapability(capability.block(), blockEntity.getBlockPos(), facing);
            return value == null ? LazyOptional.empty() : LazyOptional.of(() -> value);
        }
        if (provider instanceof Entity entity && capability.entity() != null) {
            T value = entity.getCapability(capability.entity(), facing);
            return value == null ? LazyOptional.empty() : LazyOptional.of(() -> value);
        }
        if (provider instanceof ItemStack stack && capability.item() != null) {
            @SuppressWarnings("unchecked")
            T value = (T) stack.getCapability(capability.item());
            return value == null ? LazyOptional.empty() : LazyOptional.of(() -> value);
        }
        return LazyOptional.empty();
    }
}
