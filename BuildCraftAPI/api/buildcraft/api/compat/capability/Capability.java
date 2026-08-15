package buildcraft.api.compat.capability;

import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

/**
 * Small compatibility wrapper that preserves BuildCraft's pre-1.21 capability API internally while delegating
 * external lookups to NeoForge capability objects.
 */
public final class Capability<T> {
    private final Class<T> type;
    @Nullable private final BlockCapability<T, Direction> block;
    @Nullable private final EntityCapability<T, Direction> entity;
    @Nullable private final ItemCapability<? extends T, Void> item;

    Capability(Class<T> type) {
        this.type = type;
        String name = type.getName().toLowerCase(Locale.ROOT).replace('.', '_').replace('$', '_');
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("buildcraft", "legacy/" + name);
        this.block = BlockCapability.createSided(id, type);
        this.entity = EntityCapability.createSided(id, type);
        this.item = null;
        LegacyCapabilityRegistry.add(this);
    }

    Capability(Class<T> type, @Nullable BlockCapability<T, Direction> block,
               @Nullable EntityCapability<T, Direction> entity,
               @Nullable ItemCapability<? extends T, Void> item) {
        this.type = type;
        this.block = block;
        this.entity = entity;
        this.item = item;
        LegacyCapabilityRegistry.add(this);
    }

    Capability(Class<T> type, BlockCapability<T, Direction> block) {
        this(type, block, null, null);
    }

    public Class<T> type() {
        return type;
    }

    @Nullable
    public BlockCapability<T, Direction> block() {
        return block;
    }

    @Nullable
    public EntityCapability<T, Direction> entity() {
        return entity;
    }

    @Nullable
    public ItemCapability<? extends T, Void> item() {
        return item;
    }

    @SuppressWarnings("unchecked")
    public <R> R cast(Object value) {
        return (R) value;
    }
}
