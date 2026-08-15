package buildcraft.lib.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Wire envelope for BuildCraft's existing IMessage protocol. */
public record LegacyMessagePayload(String messageClass, byte[] data) implements CustomPacketPayload {
    public static final Type<LegacyMessagePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("buildcraftlib", "legacy_message"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LegacyMessagePayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeUtf(payload.messageClass());
                buf.writeByteArray(payload.data());
            },
            buf -> new LegacyMessagePayload(buf.readUtf(), buf.readByteArray())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
