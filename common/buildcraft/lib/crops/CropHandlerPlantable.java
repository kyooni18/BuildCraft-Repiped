/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.crops;

import buildcraft.api.crops.CropManager;
import buildcraft.api.crops.ICropHandler;
import buildcraft.lib.misc.BlockUtil;
import buildcraft.lib.misc.FakePlayerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.SpecialPlantable;

public enum CropHandlerPlantable implements ICropHandler {
    INSTANCE;

    @Override
    public boolean isSeed(ItemStack stack) {
        if (stack.getItem() instanceof SpecialPlantable) {
            return true;
        }
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return block != Blocks.SUGAR_CANE && block instanceof BushBlock;
        }
        return false;
    }

    @Override
    public boolean canSustainPlant(Level world, ItemStack seed, BlockPos pos) {
        BlockPos plantPos = pos.above();
        if (!world.isEmptyBlock(plantPos)) return false;
        if (seed.getItem() instanceof SpecialPlantable special) {
            return special.canPlacePlantAtPosition(seed, world, plantPos, Direction.DOWN);
        }
        if (seed.getItem() instanceof BlockItem blockItem) {
            BlockState plantState = blockItem.getBlock().defaultBlockState();
            return plantState.canSurvive(world, plantPos) && world.getBlockState(pos).getBlock() != blockItem.getBlock();
        }
        return false;
    }

    @Override
    public boolean plantCrop(Level world, Player player, ItemStack seed, BlockPos pos) {
        return BlockUtil.useItemOnBlock(world, player, seed, pos, Direction.UP);
    }

    @Override
    public boolean isMature(LevelAccessor blockAccess, BlockState state, BlockPos pos) {
        Block block = state.getBlock();
//        if (block instanceof BlockFlower || block instanceof BlockTallGrass || block instanceof BlockMelon || block instanceof BlockMushroom || block instanceof BlockDoublePlant
        if (block instanceof FlowerBlock
                || block instanceof TallGrassBlock
                || block == Blocks.MELON
                || block instanceof MushroomBlock
                || block instanceof DoublePlantBlock
                || block == Blocks.PUMPKIN) {
            return true;
        }
//        else if (block instanceof BlockCrops)
        else if (block instanceof CropBlock) {
//            return ((BlockCrops) block).isMaxAge(state);
            return ((CropBlock) block).isMaxAge(state);
        }
//        else if (block instanceof BlockNetherWart)
        else if (block instanceof NetherWartBlock) {
//            return state.getValue(BlockNetherWart.AGE) == 3;
            return state.getValue(NetherWartBlock.AGE) == 3;
        } else if (block instanceof BushBlock) {
            if (blockAccess.getBlockState(pos.below()).getBlock() == block) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CropManager.HarvestResult harvestCrop(Level world, BlockPos pos, ItemStack tool, NonNullList<ItemStack> drops) {
//        if (!world.isRemote) {
//            IBlockState state = world.getBlockState(pos);
//            if (BlockUtil.breakBlock((ServerLevel) world, pos, drops, pos)) {
//                SoundUtil.playBlockBreak(world, pos, state);
//                return true;
//            }
//        }
        // return false;
        return BlockUtil.harvestBlock((ServerLevel) world, pos, tool, FakePlayerProvider.NULL_PROFILE) ? CropManager.HarvestResult.SUCCESS : CropManager.HarvestResult.FAIL;
    }
}
