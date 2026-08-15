/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 */
package buildcraft.lib.chunkload;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import buildcraft.lib.BCLib;
import buildcraft.lib.BCLibConfig;
import buildcraft.lib.misc.data.WorldPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.common.world.chunk.TicketSet;

/** Persistent NeoForge chunk tickets for BuildCraft chunk-loading block entities. */
public final class ChunkLoaderManager {
    private ChunkLoaderManager() {}

    public static final TicketController CONTROLLER = new TicketController(
            ResourceLocation.fromNamespaceAndPath(BCLib.MODID, "chunkloader"),
            ChunkLoaderManager::rebindTickets);

    /** Runtime mirror used to efficiently release chunks when a tile changes its working area. */
    private static final Map<WorldPos, LongSet> TICKETS = new HashMap<>();

    public static void registerTicketController(IEventBus modBus) {
        modBus.addListener((RegisterTicketControllersEvent event) -> event.register(CONTROLLER));
    }

    public static <T extends BlockEntity & IChunkLoadingTile> void loadChunksForTile(T tile) {
        if (!(tile.getLevel() instanceof ServerLevel)) return;
        if (!canLoadFor(tile)) {
            releaseChunksFor(tile);
            return;
        }
        updateChunksFor(tile);
    }

    public static <T extends BlockEntity & IChunkLoadingTile> void releaseChunksFor(T tile) {
        if (!(tile.getLevel() instanceof ServerLevel level)) return;
        WorldPos ownerKey = new WorldPos(tile);
        LongSet old = TICKETS.remove(ownerKey);
        if (old != null) {
            old.forEach(chunk -> {
                ChunkPos pos = new ChunkPos(chunk);
                CONTROLLER.forceChunk(level, tile.getBlockPos(), pos.x, pos.z, false, true);
            });
        } else {
            // If the runtime mirror has not been rebuilt yet, unforce the currently requested area.
            for (ChunkPos pos : getChunksToLoad(tile)) {
                CONTROLLER.forceChunk(level, tile.getBlockPos(), pos.x, pos.z, false, true);
            }
        }
    }

    private static <T extends BlockEntity & IChunkLoadingTile> void updateChunksFor(T tile) {
        if (!(tile.getLevel() instanceof ServerLevel level)) return;
        WorldPos ownerKey = new WorldPos(tile);
        LongSet old = TICKETS.computeIfAbsent(ownerKey, ignored -> new LongOpenHashSet());
        Set<ChunkPos> wanted = getChunksToLoad(tile);
        LongSet wantedLong = new LongOpenHashSet();
        wanted.forEach(pos -> wantedLong.add(pos.toLong()));

        for (long packed : old.toLongArray()) {
            if (!wantedLong.contains(packed)) {
                ChunkPos pos = new ChunkPos(packed);
                CONTROLLER.forceChunk(level, tile.getBlockPos(), pos.x, pos.z, false, true);
                old.remove(packed);
            }
        }
        for (ChunkPos pos : wanted) {
            if (old.add(pos.toLong())) {
                CONTROLLER.forceChunk(level, tile.getBlockPos(), pos.x, pos.z, true, true);
            }
        }
    }

    public static boolean unforceChunk(ServerLevel level, BlockPos owner, ChunkPos chunkPos) {
        return CONTROLLER.forceChunk(level, owner, chunkPos.x, chunkPos.z, false, true);
    }

    public static boolean forceChunk(ServerLevel level, BlockPos owner, ChunkPos chunkPos) {
        return CONTROLLER.forceChunk(level, owner, chunkPos.x, chunkPos.z, true, true);
    }

    public static <T extends BlockEntity & IChunkLoadingTile> Set<ChunkPos> getChunksToLoad(T tile) {
        Set<ChunkPos> requested = tile.getChunksToLoad();
        Set<ChunkPos> result = new HashSet<>(requested != null ? requested : Collections.emptySet());
        result.add(new ChunkPos(tile.getBlockPos()));
        return result;
    }

    /** Validates persisted tickets and rebuilds the runtime mirror after a world is loaded. */
    public static void rebindTickets(ServerLevel level, net.neoforged.neoforge.common.world.chunk.TicketHelper helper) {
        TICKETS.entrySet().removeIf(e -> e.getKey().dimension.equals(level.dimension().location().toString()));
        for (Map.Entry<BlockPos, TicketSet> entry : helper.getBlockTickets().entrySet()) {
            BlockPos owner = entry.getKey();
            BlockEntity tile = level.getBlockEntity(owner);
            if (!(tile instanceof IChunkLoadingTile loading) || !canLoadFor(loading)) {
                helper.removeAllTickets(owner);
                continue;
            }
            LongSet loaded = new LongOpenHashSet();
            loaded.addAll(entry.getValue().ticking());
            loaded.addAll(entry.getValue().nonTicking());
            TICKETS.put(new WorldPos(level, owner), loaded);

            // Reconcile saved tickets against the tile's current requested area/configuration.
            @SuppressWarnings("unchecked")
            BlockEntity castTile = tile;
            reconcileLoadedTile(level, owner, castTile, loading, loaded);
        }
    }

    private static <T extends BlockEntity & IChunkLoadingTile> void reconcileLoadedTile(
            ServerLevel level, BlockPos owner, BlockEntity rawTile, IChunkLoadingTile loading, LongSet loaded) {
        @SuppressWarnings("unchecked") T tile = (T) rawTile;
        Set<ChunkPos> wanted = getChunksToLoad(tile);
        LongSet wantedLong = new LongOpenHashSet();
        wanted.forEach(p -> wantedLong.add(p.toLong()));
        for (long packed : loaded.toLongArray()) {
            if (!wantedLong.contains(packed)) {
                ChunkPos p = new ChunkPos(packed);
                CONTROLLER.forceChunk(level, owner, p.x, p.z, false, true);
                loaded.remove(packed);
            }
        }
        for (ChunkPos p : wanted) {
            if (loaded.add(p.toLong())) CONTROLLER.forceChunk(level, owner, p.x, p.z, true, true);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean canLoadFor(IChunkLoadingTile tile) {
        return BCLibConfig.chunkLoadingLevel.canLoad(tile.getLoadType());
    }
}
