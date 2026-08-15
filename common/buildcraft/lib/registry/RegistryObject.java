package buildcraft.lib.registry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class RegistryObject<T> implements Supplier<T> {
    private final DeferredHolder<?, ?> holder;
    public RegistryObject(DeferredHolder<?, ?> holder) { this.holder = holder; }
    @SuppressWarnings("unchecked") public T get() { return (T) holder.get(); }
    public boolean isPresent() { return holder.isBound(); }
    public ResourceLocation getId() { return holder.getId(); }
    public static <T> RegistryObject<T> of(DeferredHolder<?, ?> holder) { return new RegistryObject<>(holder); }
}
