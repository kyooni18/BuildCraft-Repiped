package buildcraft.api.compat.capability;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CapabilityToken<T> {
    private final Class<T> type;
    @SuppressWarnings("unchecked")
    protected CapabilityToken() {
        Type s = getClass().getGenericSuperclass();
        if (!(s instanceof ParameterizedType p) || !(p.getActualTypeArguments()[0] instanceof Class<?> c)) {
            throw new IllegalStateException("CapabilityToken must use a concrete class/interface type");
        }
        this.type = (Class<T>) c;
    }
    public Class<T> type() { return type; }
}
