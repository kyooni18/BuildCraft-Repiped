package buildcraft.api.net;

import java.util.concurrent.CompletableFuture;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Small compatibility wrapper around NeoForge's payload context. */
public final class MessageContext {
    private final IPayloadContext delegate;

    public MessageContext(IPayloadContext delegate) {
        this.delegate = delegate;
    }

    public boolean isServerSide() {
        return delegate.flow() == PacketFlow.SERVERBOUND;
    }

    public boolean isClientSide() {
        return delegate.flow() == PacketFlow.CLIENTBOUND;
    }

    public Player getSender() {
        return delegate.player();
    }

    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return delegate.enqueueWork(task);
    }

    /** NeoForge payload handlers are considered handled once their handler returns. */
    public void setPacketHandled(boolean handled) {
    }

    public IPayloadContext unwrap() {
        return delegate;
    }
}
