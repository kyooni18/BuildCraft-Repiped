package buildcraft.api.compat.capability;

import buildcraft.api.compat.LazyOptional;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface ICapabilityProvider {
    default <T> LazyOptional<T> getCapability(Capability<T> capability) { return getCapability(capability, null); }
    default <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) { return LazyOptional.empty(); }
}
