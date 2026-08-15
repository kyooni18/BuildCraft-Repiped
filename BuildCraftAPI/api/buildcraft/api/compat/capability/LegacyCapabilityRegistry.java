package buildcraft.api.compat.capability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/** Tracks BuildCraft's compatibility capabilities and exposes them through the real NeoForge capability registry. */
public final class LegacyCapabilityRegistry {
    private static final List<Capability<?>> ALL = new ArrayList<>();

    static synchronized void add(Capability<?> c) {
        if (!ALL.contains(c)) ALL.add(c);
    }

    public static synchronized List<Capability<?>> all() {
        return Collections.unmodifiableList(new ArrayList<>(ALL));
    }

    /**
     * Register the compatibility bridge for every BuildCraft block/entity type. Registration is intentionally limited
     * to BuildCraft namespaces so this shim never captures another mod's providers.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerNeoForgeProviders(RegisterCapabilitiesEvent event) {
        List<Capability<?>> caps = all();
        for (BlockEntityType<?> type : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
            ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type);
            if (id == null || !id.getNamespace().startsWith("buildcraft")) continue;
            for (Capability<?> cap : caps) {
                BlockCapability block = cap.block();
                if (block == null) continue;
                event.registerBlockEntity(block, (BlockEntityType) type, (be, context) -> {
                    if (be instanceof ICapabilityProvider provider) {
                        return provider.getCapability((Capability) cap, (Direction) context).orElse(null);
                    }
                    return null;
                });
            }
        }
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            if (id == null || !id.getNamespace().startsWith("buildcraft")) continue;
            for (Capability<?> cap : caps) {
                EntityCapability entity = cap.entity();
                if (entity == null) continue;
                event.registerEntity(entity, (EntityType) type, (e, context) -> {
                    if (e instanceof ICapabilityProvider provider) {
                        return provider.getCapability((Capability) cap, (Direction) context).orElse(null);
                    }
                    return null;
                });
            }
        }
    }

    private LegacyCapabilityRegistry() {}
}
