package buildcraft.api.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.stream.Stream;

/** Registry lookup provider backed by Minecraft's built-in (static) registries. */
public final class BuiltinRegistryProvider {
    public static final HolderLookup.Provider INSTANCE = new HolderLookup.Provider() {
        @Override
        public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
            return BuiltInRegistries.REGISTRY.entrySet().stream().map(entry -> (ResourceKey<? extends Registry<?>>) entry.getKey());
        }

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
            Registry registry = BuiltInRegistries.REGISTRY.get(registryKey.location());
            return registry == null ? Optional.empty() : Optional.of((HolderLookup.RegistryLookup<T>) registry.asLookup());
        }
    };

    private BuiltinRegistryProvider() {
    }
}
