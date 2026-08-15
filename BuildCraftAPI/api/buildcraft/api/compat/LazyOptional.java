package buildcraft.api.compat;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class LazyOptional<T> {
    private static final LazyOptional<?> EMPTY = new LazyOptional<>(null, false);
    private Supplier<? extends T> supplier;
    private T value;
    private boolean resolved;
    private boolean valid;

    private LazyOptional(Supplier<? extends T> supplier, boolean valid) {
        this.supplier = supplier;
        this.valid = valid;
    }

    public static <T> LazyOptional<T> of(Supplier<? extends T> supplier) {
        if (supplier == null) throw new NullPointerException("supplier");
        return new LazyOptional<>(supplier, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> LazyOptional<T> empty() { return (LazyOptional<T>) EMPTY; }

    private T valueOrNull() {
        if (!valid) return null;
        if (!resolved) {
            resolved = true;
            value = supplier == null ? null : supplier.get();
            supplier = null;
        }
        return value;
    }

    public boolean isPresent() { return valueOrNull() != null; }
    public T orElse(T other) { T v = valueOrNull(); return v == null ? other : v; }
    public Optional<T> resolve() { return Optional.ofNullable(valueOrNull()); }
    public void ifPresent(Consumer<? super T> consumer) { T v = valueOrNull(); if (v != null) consumer.accept(v); }
    public void invalidate() { valid = false; value = null; supplier = null; resolved = true; }
    @SuppressWarnings("unchecked") public <R> LazyOptional<R> cast() { return (LazyOptional<R>) this; }
}
