/* Copyright (c) 2016 SpaceToad and the BuildCraft team
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package buildcraft.lib.net;

import buildcraft.api.IBuildCraftMod;
import buildcraft.api.core.BCDebugging;
import buildcraft.api.core.BCLog;
import buildcraft.api.net.IMessage;
import buildcraft.api.net.IMessageHandler;
import buildcraft.api.net.MessageContext;
import buildcraft.lib.BCLibProxy;
import buildcraft.lib.misc.MessageUtil;
import io.netty.buffer.Unpooled;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * BuildCraft's legacy message registry, transported through a single NeoForge custom payload.
 * The IMessage serialization format is intentionally retained to avoid rewriting every packet class.
 */
public class MessageManager {
    public static final boolean DEBUG = BCDebugging.shouldDebugLog("lib.messages");

    private static final Map<IBuildCraftMod, PerModHandler> MOD_HANDLERS =
            new ConcurrentSkipListMap<>(MessageManager::compareMods);
    private static final Map<Class<? extends IMessage>, PerMessageInfo<?>> MESSAGE_HANDLERS = new ConcurrentHashMap<>();
    private static final Map<String, PerMessageInfo<?>> MESSAGE_HANDLERS_BY_NAME = new ConcurrentHashMap<>();
    private static volatile boolean payloadRegistered;

    static class PerModHandler {
        final IBuildCraftMod module;
        final SortedMap<Class<? extends IMessage>, PerMessageInfo<?>> knownMessages =
                new TreeMap<>(Comparator.comparing(Class::getName));

        PerModHandler(IBuildCraftMod module) {
            this.module = module;
        }
    }

    static class PerMessageInfo<I extends IMessage> {
        final PerModHandler modHandler;
        final Class<I> messageClass;
        @Nullable IMessageHandler<I, ?> clientHandler, serverHandler;

        PerMessageInfo(PerModHandler modHandler, Class<I> messageClass) {
            this.modHandler = modHandler;
            this.messageClass = messageClass;
        }
    }

    private static int compareMods(IBuildCraftMod modA, IBuildCraftMod modB) {
        if (modA instanceof Enum && modB instanceof Enum) {
            Enum<?> enumA = (Enum<?>) modA;
            Enum<?> enumB = (Enum<?>) modB;
            if (enumA.getDeclaringClass() == enumB.getDeclaringClass()) {
                return Integer.compare(enumA.ordinal(), enumB.ordinal());
            }
        }
        return modA.getModId().compareTo(modB.getModId());
    }

    public static <I extends IMessage> void registerMessageClass(IBuildCraftMod module, Class<I> clazz, Dist... sides) {
        registerMessageClass(module, clazz, null, sides);
    }

    public static <I extends IMessage> void registerMessageClass(
            IBuildCraftMod module, Class<I> messageClass, IMessageHandler<I, ?> messageHandler, Dist... sides) {
        PerModHandler modHandler = MOD_HANDLERS.computeIfAbsent(module, PerModHandler::new);
        @SuppressWarnings("unchecked")
        PerMessageInfo<I> messageInfo = (PerMessageInfo<I>) modHandler.knownMessages.get(messageClass);
        if (messageInfo == null) {
            messageInfo = new PerMessageInfo<>(modHandler, messageClass);
            modHandler.knownMessages.put(messageClass, messageInfo);
            MESSAGE_HANDLERS.put(messageClass, messageInfo);
            MESSAGE_HANDLERS_BY_NAME.put(messageClass.getName(), messageInfo);
        }
        if (messageHandler == null) {
            return;
        }
        Dist specificSide = sides != null && sides.length == 1 ? sides[0] : null;
        if (specificSide == null || specificSide == Dist.CLIENT) {
            messageInfo.clientHandler = messageHandler;
        }
        if (specificSide == null || specificSide == Dist.DEDICATED_SERVER) {
            messageInfo.serverHandler = messageHandler;
        }
    }

    public static <I extends IMessage> void setHandler(
            Class<I> messageClass, IMessageHandler<I, ?> messageHandler, Dist side) {
        @SuppressWarnings("unchecked")
        PerMessageInfo<I> messageInfo = (PerMessageInfo<I>) MESSAGE_HANDLERS.get(messageClass);
        if (messageInfo == null) {
            throw new IllegalArgumentException("Cannot set handler for unregistered message: " + messageClass);
        }
        registerMessageClass(messageInfo.modHandler.module, messageClass, messageHandler, side);
    }

    /** NeoForge mod-bus registration hook. */
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        if (payloadRegistered) {
            return;
        }
        payloadRegistered = true;
        event.registrar("1").playBidirectional(
                LegacyMessagePayload.TYPE,
                LegacyMessagePayload.STREAM_CODEC,
                MessageManager::handlePayload
        );
    }

    private static void handlePayload(LegacyMessagePayload payload, net.neoforged.neoforge.network.handling.IPayloadContext neoContext) {
        PerMessageInfo<?> rawInfo = MESSAGE_HANDLERS_BY_NAME.get(payload.messageClass());
        if (rawInfo == null) {
            BCLog.logger.warn("Received unregistered BuildCraft message {}", payload.messageClass());
            return;
        }
        handlePayloadTyped(rawInfo, payload, new MessageContext(neoContext));
    }

    private static <I extends IMessage> void handlePayloadTyped(
            PerMessageInfo<I> info, LegacyMessagePayload payload, MessageContext context) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
        I message = IMessage.staticFromBytes(info.messageClass, buf);
        if (message == null) {
            return;
        }
        IMessageHandler<I, ?> handler = context.isServerSide() ? info.serverHandler : info.clientHandler;
        IMessage reply = wrapHandler(handler, info.messageClass).onMessage(message, context);
        if (reply != null) {
            MessageUtil.sendReturnMessage(context, reply);
        }
    }

    /** Final consistency check retained from the old channel-registration phase. */
    public static void fmlPostInit() {
        for (PerModHandler handler : MOD_HANDLERS.values()) {
            for (PerMessageInfo<?> info : handler.knownMessages.values()) {
                if (info.clientHandler == null && info.serverHandler == null && FMLEnvironment.dist == Dist.CLIENT) {
                    throw new IllegalStateException("Registered message has no handlers: " + info.messageClass.getName());
                }
            }
        }
    }

    private static <I extends IMessage> IMessageHandler<I, ?> wrapHandler(
            IMessageHandler<I, ?> messageHandler, Class<I> messageClass) {
        if (messageHandler == null) {
            return (message, context) -> {
                if (context.isServerSide()) {
                    Player player = context.getSender();
                    BCLog.logger.warn("Client {} sent invalid BuildCraft message {}", player, messageClass.getName());
                } else {
                    BCLog.logger.error("Received server-only BuildCraft message {} on client", messageClass.getName());
                }
                return null;
            };
        }
        return (message, context) -> {
            Player player = BCLibProxy.getProxy().getPlayerForContext(context);
            if (player == null || player.level() == null) {
                return null;
            }
            BCLibProxy.getProxy().addScheduledTask(player.level(), () -> {
                IMessage reply = messageHandler.onMessage(message, context);
                if (reply != null) {
                    MessageUtil.sendReturnMessage(context, reply);
                }
            });
            return null;
        };
    }

    private static LegacyMessagePayload toPayload(IMessage message) {
        if (!MESSAGE_HANDLERS.containsKey(message.getClass())) {
            throw new IllegalArgumentException("Cannot send unregistered message " + message.getClass());
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        message.toBytes(buf);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        return new LegacyMessagePayload(message.getClass().getName(), bytes);
    }

    public static void sendToAll(IMessage message) {
        PacketDistributor.sendToAllPlayers(toPayload(message));
    }

    public static void sendTo(IMessage message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, toPayload(message));
    }

    public static void sendToAllAround(
            IMessage message, ServerLevel level, @Nullable ServerPlayer excluded,
            double x, double y, double z, double radius) {
        PacketDistributor.sendToPlayersNear(level, excluded, x, y, z, radius, toPayload(message));
    }

    public static void sendToDimension(IMessage message, ResourceKey<Level> dimensionId) {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            return;
        }
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().getLevel(dimensionId);
        if (level != null) {
            PacketDistributor.sendToPlayersInDimension(level, toPayload(message));
        }
    }

    public static void sendToServer(IMessage message) {
        PacketDistributor.sendToServer(toPayload(message));
    }

    public static void sendToEntity(IMessage message, Entity entity) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, toPayload(message));
    }
}
