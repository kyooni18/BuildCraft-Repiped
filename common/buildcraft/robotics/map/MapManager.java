package buildcraft.robotics.map;

import buildcraft.lib.misc.data.XorShift128Random;
import com.google.common.collect.HashBiMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;

import java.io.File;
import java.util.Date;

public class MapManager implements Runnable {
    private static final int UPDATE_DELAY = 60000;
    private final HashBiMap<Level, MapWorld> worldMap = HashBiMap.create();
    private final File location;
    private boolean stop = false;
    private long lastSaveTime;
    public static final XorShift128Random rand = new XorShift128Random();

    public MapManager(File location) {
        this.location = location;
    }

    public void stop() {
        stop = true;
        saveAllWorlds();
    }

    public MapWorld getWorld(LevelAccessor levelAccessor) {
        if (!(levelAccessor instanceof Level)) {
            return null;
        }
        Level world = (Level) levelAccessor;
        if (world == null || world.isClientSide) {
            return null;
        }

        if (!worldMap.containsKey(world)) {
            synchronized (worldMap) {
                worldMap.put(world, new MapWorld(world, location));
            }
        }
        return worldMap.get(world);
    }

    private static boolean doUpdate(MapWorld world, ChunkAccess chunk) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;
        long updateTime = (new Date()).getTime() - UPDATE_DELAY;
        return world.getUpdateTime(x, z) < updateTime || !world.hasChunk(x, z);
    }

    private void updateChunk(LevelAccessor rworld, ChunkAccess chunk, boolean force) {
        MapWorld world = getWorld(rworld);
        // if (world != null && (force || doUpdate(world, chunk)))
        if (world != null && (chunk instanceof LevelChunk) && (force || doUpdate(world, chunk))) {
            world.updateChunk((LevelChunk) chunk);
        }
    }

    private void updateChunkDelayed(LevelAccessor rworld, ChunkAccess chunk, boolean force, byte time) {
        MapWorld world = getWorld(rworld);
        if (world != null && (force || doUpdate(world, chunk))) {
            world.updateChunkDelayed(chunk, time);
        }
    }

    @SubscribeEvent
    public void tickDelayedWorlds(LevelTickEvent.Post event) {
        if (!event.getLevel().isClientSide) {
            MapWorld w = worldMap.get(event.getLevel());
            if (w != null) {
                w.tick();
            }
        }
    }

    @SubscribeEvent
    public void worldUnloaded(LevelEvent.Unload event) {
        if (worldMap.containsKey(event.getLevel())) {
            worldMap.get(event.getLevel()).save();
            synchronized (worldMap) {
                worldMap.remove(event.getLevel());
            }
        }
    }

    @SubscribeEvent
    public void chunkLoaded(ChunkEvent.Load event) {
        // updateChunkDelayed(event.getWorld(), event.getChunk(), false, (byte) (40 + VecUtil.RANDOM.nextInt(20)));
        updateChunkDelayed(event.getLevel(), event.getChunk(), false, (byte) (40 + rand.nextInt(20)));
    }

    @SubscribeEvent
    public void chunkUnloaded(ChunkEvent.Unload event) {
        updateChunk(event.getLevel(), event.getChunk(), false);
    }

    @SubscribeEvent
    // public void blockPlaced(BlockEvent.PlaceEvent placeEvent)
    public void blockPlaced(BlockEvent.EntityPlaceEvent placeEvent) {
        // LevelChunk chunk = placeEvent.world.getChunkFromBlockCoords(placeEvent.pos);
        ChunkAccess chunk = placeEvent.getLevel().getChunk(placeEvent.getPos());
        // MapWorld world = getWorld(placeEvent.world);
        MapWorld world = getWorld(placeEvent.getLevel());
        // if (world != null && doUpdate(world, chunk))
        if (world != null && chunk instanceof LevelChunk && doUpdate(world, chunk)) {
            // int hv = placeEvent.world.getHeight(placeEvent.pos).getY();
            int hv = placeEvent.getLevel().getHeight(Heightmap.Types.WORLD_SURFACE, placeEvent.getPos().getX(), placeEvent.getPos().getZ());
            // if (placeEvent.pos.getY() >= (hv - 3))
            if (placeEvent.getPos().getY() >= (hv - 3)) {
                world.updateChunk((LevelChunk) chunk);
            }
        }
    }

    @SubscribeEvent
    public void blockBroken(BlockEvent.BreakEvent placeEvent) {
        // Chunk chunk = placeEvent.world.getChunkFromBlockCoords(placeEvent.pos);
        ChunkAccess chunk = placeEvent.getLevel().getChunk(placeEvent.getPos());
        MapWorld world = getWorld(placeEvent.getLevel());
        // if (world != null && doUpdate(world, chunk))
        if (world != null && chunk instanceof LevelChunk && doUpdate(world, chunk)) {
            // int hv = placeEvent.getWorld().getHeight(placeEvent.getPos()).getY();
            int hv = placeEvent.getLevel().getHeight(Heightmap.Types.WORLD_SURFACE, placeEvent.getPos().getX(), placeEvent.getPos().getZ());
            if (placeEvent.getPos().getY() >= (hv - 3)) {
                world.updateChunk((LevelChunk) chunk);
            }
        }
    }

    public void saveAllWorlds() {
        synchronized (worldMap) {
            for (MapWorld world : worldMap.values()) {
                world.save();
            }
        }
    }

    @Override
    public void run() {
        lastSaveTime = (new Date()).getTime();

        while (!stop) {
            long now = (new Date()).getTime();

            if (now - lastSaveTime > 120000) {
                saveAllWorlds();
                lastSaveTime = now;
            }

            try {
                Thread.sleep(4000);
            } catch (Exception e) {

            }
        }
    }

    // public void initialize()
    public void initialize(ServerStartingEvent event) {
        // for (WorldServer ws : DimensionManager.getWorlds())
        for (ServerLevel ws : event.getServer().getAllLevels()) {
            MapWorld mw = getWorld(ws);
            // IChunkProvider provider = ws.getChunkProvider();
            ChunkSource provider = ws.getChunkSource();
            // if (provider instanceof ChunkProviderServer)
            if (provider instanceof ServerChunkCache) {
                // for (Object o : ((ChunkProviderServer) provider).func_152380_a())
                for (ChunkHolder o : ((ServerChunkCache) provider).chunkMap.getChunks()) {
                    // if (o != null && o instanceof Chunk)
                    if (o != null) {
                        // Chunk c = (Chunk) o;
                        ChunkAccess c = o.getTickingChunk();
                        if (c == null) {
                            continue;
                        }
                        // if (!mw.hasChunk(c.xPosition, c.zPosition))
                        if (!mw.hasChunk(c.getPos().x, c.getPos().z)) {
                            // mw.updateChunkDelayed(c, (byte) (40 + VecUtil.RANDOM.nextInt(20)));
                            mw.updateChunkDelayed(c, (byte) (40 + rand.nextInt(20)));
                        }
                    }
                }
            }
        }
    }
}
