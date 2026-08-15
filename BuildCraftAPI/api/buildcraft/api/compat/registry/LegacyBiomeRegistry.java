package buildcraft.api.compat.registry;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

/** Compatibility view only. Dynamic biome lookups must use a live RegistryAccess. */
public final class LegacyBiomeRegistry {
    public Set<ResourceLocation> getKeys() { return Collections.emptySet(); }
    public boolean containsKey(ResourceLocation id) { return false; }
    public Biome getValue(ResourceLocation id) { return null; }
    public Optional<Holder.Reference<Biome>> getHolder(ResourceKey<Biome> key) { return Optional.empty(); }
}
