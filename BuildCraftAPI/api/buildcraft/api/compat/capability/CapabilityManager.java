package buildcraft.api.compat.capability;
public final class CapabilityManager {
    private CapabilityManager() {}
    public static <T> Capability<T> get(CapabilityToken<T> token) { return new Capability<>(token.type()); }
}
