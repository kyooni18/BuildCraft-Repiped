/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.transport;

import buildcraft.transport.net.PipeItemMessageQueue;
import buildcraft.transport.wire.WorldSavedDataWireSystems;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.bus.api.SubscribeEvent;

public enum BCTransportEventDist {
    INSTANCE;

    @SubscribeEvent
    public void onWorldTickPre(LevelTickEvent.Pre event) {
        onWorldTick(event);
    }

    @SubscribeEvent
    public void onWorldTickPost(LevelTickEvent.Post event) {
        onWorldTick(event);
    }

    private void onWorldTick(LevelTickEvent event) {
//        if (!event.world.isRemote && event.world.getMinecraftServer() != null)
        if (!event.getLevel().isClientSide && event.getLevel().getServer() != null) {
            WorldSavedDataWireSystems.get(event.getLevel()).tick();
        }
    }

    @SubscribeEvent
    public void onServerTickPre(ServerTickEvent.Pre event) {
        PipeItemMessageQueue.serverTick();
    }

    @SubscribeEvent
    public void onServerTickPost(ServerTickEvent.Post event) {
        PipeItemMessageQueue.serverTick();
    }

    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.Sent event) {
        WorldSavedDataWireSystems.get(event.getPlayer().level()).changedPlayers.add(event.getPlayer());
    }

    @SubscribeEvent
//    public void onBlockPlace(BlockEvent.PlaceEvent event)
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // event.setCanceled(true);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        // event.setCanceled(true);
    }
}
