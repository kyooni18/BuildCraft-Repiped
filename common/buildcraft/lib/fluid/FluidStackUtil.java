package buildcraft.lib.fluid;

import buildcraft.api.compat.BuiltinRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;


/** Compatibility helpers for the registry-aware FluidStack codec introduced in 1.21. */
public final class FluidStackUtil {
    private static final HolderLookup.Provider STATIC_REGISTRIES = BuiltinRegistryProvider.INSTANCE;

    private FluidStackUtil() {
    }

    public static FluidStack load(CompoundTag tag) {
        return FluidStack.parseOptional(STATIC_REGISTRIES, tag);
    }

    public static CompoundTag save(FluidStack stack) {
        return stack.isEmpty() ? new CompoundTag() : (CompoundTag) stack.save(STATIC_REGISTRIES, new CompoundTag());
    }

    public static CompoundTag save(FluidStack stack, CompoundTag target) {
        if (!stack.isEmpty()) {
            stack.save(STATIC_REGISTRIES, target);
        }
        return target;
    }

    public static HolderLookup.Provider provider() {
        return STATIC_REGISTRIES;
    }
}
