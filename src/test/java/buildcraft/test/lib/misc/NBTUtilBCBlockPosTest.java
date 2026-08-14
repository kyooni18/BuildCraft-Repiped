/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 */
package buildcraft.test.lib.misc;

import buildcraft.lib.misc.NBTUtilBC;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.junit.Assert;
import org.junit.Test;

public class NBTUtilBCBlockPosTest {
    @Test
    public void readsCurrentIntArrayFormatFromParent() {
        BlockPos expected = new BlockPos(123, -45, 6789);
        CompoundTag parent = new CompoundTag();
        parent.put("pos", NBTUtilBC.writeBlockPos(expected));
        Assert.assertEquals(expected, NBTUtilBC.readBlockPos(parent, "pos"));
    }

    @Test
    public void readsLegacyCompoundFormatFromParent() {
        BlockPos expected = new BlockPos(-7, 64, 91);
        CompoundTag legacy = new CompoundTag();
        legacy.putInt("x", expected.getX());
        legacy.putInt("y", expected.getY());
        legacy.putInt("z", expected.getZ());
        CompoundTag parent = new CompoundTag();
        parent.put("pos", legacy);
        Assert.assertEquals(expected, NBTUtilBC.readBlockPos(parent, "pos"));
    }

    @Test
    public void missingPositionReturnsNull() {
        Assert.assertNull(NBTUtilBC.readBlockPos(new CompoundTag(), "pos"));
    }
}
