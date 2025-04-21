package buildcraft.lib.blockscan;

import buildcraft.api.core.IZone;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.Iterator;
import java.util.Random;

public class BlockScannerZoneRandom implements Iterable<BlockPos> {

    private RandomSource rand;
    private IZone zone;
    private BlockPos pos;

    class BlockIt implements Iterator<BlockPos> {

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public BlockPos next() {
            BlockPos pos = zone.getRandomBlockPos(rand);
            return pos.subtract(BlockScannerZoneRandom.this.pos);
        }

        @Override
        public void remove() {}
    }

    public BlockScannerZoneRandom(BlockPos pos, RandomSource iRand, IZone iZone) {
        this.pos = pos;
        rand = iRand;
        zone = iZone;
    }

    @Override
    public Iterator<BlockPos> iterator() {
        return new BlockIt();
    }

}
