package buildcraft.api.compat.registry;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public final class LegacyRegistry<T> implements Iterable<T> {
    private final Registry<T> registry;

    public LegacyRegistry(Registry<T> registry) {
        this.registry = registry;
    }

    public T getValue(ResourceLocation id) {
        return registry.getOptional(id).orElse(null);
    }

    public ResourceLocation getKey(T value) {
        return registry.getKey(value);
    }

    public boolean containsKey(ResourceLocation id) {
        return registry.containsKey(id);
    }

    public boolean containsValue(T value) {
        ResourceLocation key = registry.getKey(value);
        return key != null && registry.getOptional(key).orElse(null) == value;
    }

    public Set<ResourceLocation> getKeys() {
        return registry.keySet();
    }

    public Collection<T> getValues() {
        return registry.stream().toList();
    }

    public Set<java.util.Map.Entry<ResourceKey<T>, T>> getEntries() {
        return registry.entrySet();
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registry.key();
    }

    public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key) {
        return registry.getHolder(key);
    }

    public TagView<T> tags() {
        return new TagView<>(registry);
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return registry.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return registry.spliterator();
    }

    public Registry<T> unwrap() {
        return registry;
    }

    public static final class TagValues<T> implements Iterable<T> {
        private final java.util.List<T> values;
        private TagValues(Stream<T> values) { this.values = values.toList(); }
        public Stream<T> stream() { return values.stream(); }
        @Override public java.util.Iterator<T> iterator() { return values.iterator(); }
    }

    public static final class TagView<T> {
        private final Registry<T> registry;

        private TagView(Registry<T> registry) {
            this.registry = registry;
        }

        public TagValues<T> getTag(TagKey<T> key) {
            return new TagValues<>(registry.getTag(key)
                    .map(named -> named.stream().map(Holder::value))
                    .orElseGet(Stream::empty));
        }

        public Stream<TagKey<T>> getTagNames() {
            return registry.getTagNames();
        }
    }
}
