package buildcraft.api.tiles;

import buildcraft.api.compat.capability.Capability;
import buildcraft.api.compat.capability.CapabilityManager;
import buildcraft.api.compat.capability.CapabilityToken;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.SubscribeEvent;

import javax.annotation.Nonnull;

public class TilesAPI {
    @Nonnull
    public static final Capability<IControllable> CAP_CONTROLLABLE = CapabilityManager.get(new CapabilityToken<>() {
    });

    @Nonnull
    public static final Capability<IHasWork> CAP_HAS_WORK = CapabilityManager.get(new CapabilityToken<>() {
    });

    @Nonnull
    public static final Capability<IHeatable> CAP_HEATABLE = CapabilityManager.get(new CapabilityToken<>() {
    });

    @Nonnull
    public static final Capability<ITileAreaProvider> CAP_TILE_AREA_PROVIDER = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        // NeoForge capability type objects are created directly; providers are registered centrally by BCLib.
    }
}
