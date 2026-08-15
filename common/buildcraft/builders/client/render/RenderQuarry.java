/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.builders.client.render;

import buildcraft.api.properties.BuildCraftProperties;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.builders.tile.TileQuarry;
import buildcraft.core.client.BuildCraftLaserManager;
import buildcraft.lib.client.render.laser.LaserBoxRenderer;
import buildcraft.lib.client.render.laser.LaserData_BC8;
import buildcraft.lib.client.render.laser.LaserRenderer_BC8;
import buildcraft.lib.client.sprite.SpriteHolderRegistry;
import buildcraft.lib.misc.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderQuarry implements BlockEntityRenderer<TileQuarry> {
    public static final LaserData_BC8.LaserType FRAME;
    public static final LaserData_BC8.LaserType FRAME_BOTTOM;
    public static final LaserData_BC8.LaserType DRILL;
    public static final LaserData_BC8.LaserType LASER;

    static {
        {
            SpriteHolderRegistry.SpriteHolder sprite = SpriteHolderRegistry.getHolder("buildcraftbuilders:block/frame/default");
            LaserData_BC8.LaserRow capStart = new LaserData_BC8.LaserRow(sprite, 0, 0, 0, 0);
            LaserData_BC8.LaserRow start = null;
            LaserData_BC8.LaserRow[] middle = { new LaserData_BC8.LaserRow(sprite, 0, 4, 16, 12) };
            LaserData_BC8.LaserRow end = new LaserData_BC8.LaserRow(sprite, 0, 4, 16, 12);
            LaserData_BC8.LaserRow capEnd = new LaserData_BC8.LaserRow(sprite, 0, 0, 0, 0);
            FRAME = new LaserData_BC8.LaserType(capStart, start, middle, end, capEnd);
        }
        {
            SpriteHolderRegistry.SpriteHolder sprite = SpriteHolderRegistry.getHolder("buildcraftbuilders:block/frame/default");
            LaserData_BC8.LaserRow capStart = new LaserData_BC8.LaserRow(sprite, 0, 0, 0, 0);
            LaserData_BC8.LaserRow start = null;
            LaserData_BC8.LaserRow[] middle = { new LaserData_BC8.LaserRow(sprite, 0, 4, 16, 12) };
            LaserData_BC8.LaserRow end = new LaserData_BC8.LaserRow(sprite, 0, 4, 16, 12);
            LaserData_BC8.LaserRow capEnd = new LaserData_BC8.LaserRow(sprite, 4, 4, 12, 12);
            FRAME_BOTTOM = new LaserData_BC8.LaserType(capStart, start, middle, end, capEnd);
        }
        {
            SpriteHolderRegistry.SpriteHolder sprite = SpriteHolderRegistry.getHolder("buildcraftbuilders:block/quarry/drill");
            LaserData_BC8.LaserRow capStart = new LaserData_BC8.LaserRow(sprite, 6, 0, 10, 4);
            LaserData_BC8.LaserRow start = null;
            LaserData_BC8.LaserRow[] middle = { new LaserData_BC8.LaserRow(sprite, 0, 0, 16, 4) };
            LaserData_BC8.LaserRow end = null;
            LaserData_BC8.LaserRow capEnd = new LaserData_BC8.LaserRow(sprite, 6, 0, 10, 4);
            DRILL = new LaserData_BC8.LaserType(capStart, start, middle, end, capEnd);
        }
        {
            LASER = BuildCraftLaserManager.POWER_LOW;
        }
    }

    public RenderQuarry(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
            TileQuarry tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
            int combinedLight, int combinedOverlay
    ) {
        // Intentionally empty. The quarry gantry, drill and mining beam use absolute world
        // coordinates and span many blocks, so rendering them from the block entity's local
        // PoseStack makes them camera/block-origin dependent in 1.21.1. They are rendered from
        // RenderLevelStageEvent by BCBuildersEventDist instead.
    }

    public static void renderWorld(
            TileQuarry tile, float partialTicks, PoseStack poseStack, VertexConsumer laserBuffer, Vec3 cameraPos
    ) {
        if (tile.getLevel() == null) {
            return;
        }
        BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());
        if (state.getBlock() != BCBuildersBlocks.quarry.get() || !tile.frameBox.isInitialized()) {
            return;
        }

        ProfilerFiller profiler = Minecraft.getInstance().getProfiler();
        profiler.push("bc");
        profiler.push("quarry");
        try {
            final BlockPos min = tile.frameBox.min();
            final BlockPos max = tile.frameBox.max();
            double yOffset = 1 + 4 / 16D;

            if (tile.currentTask instanceof TileQuarry.TaskBreakBlock taskBreakBlock) {
                BlockPos pos = taskBreakBlock.breakPos;
                if (tile.drillPos == null) {
                    if (taskBreakBlock.clientPower != 0) {
                        Vec3 from = VecUtil.convertCenter(tile.getBlockPos());
                        Vec3 to = VecUtil.convertCenter(pos);
                        LaserRenderer_BC8.renderLaserWorld(
                                new LaserData_BC8(LASER, from, to, 1 / 16.0),
                                poseStack.last(), laserBuffer, cameraPos
                        );
                    }
                } else {
                    long power = (long) (
                            taskBreakBlock.prevClientPower
                                    + (taskBreakBlock.clientPower - taskBreakBlock.prevClientPower) * (double) partialTicks
                    );
                    VoxelShape shape = tile.getLevel().getBlockState(pos).getCollisionShape(tile.getLevel(), pos);
                    AABB aabb = shape.isEmpty() ? new AABB(0, 0, 0, 0, 0, 0) : shape.bounds();
                    double value = (double) power / taskBreakBlock.getTarget();
                    if (value < 0.9) {
                        value = 1 - value / 0.9;
                    } else {
                        value = (value - 0.9) / 0.1;
                    }
                    double scaleMin = 1 - (1 - aabb.maxY) - (aabb.maxY - aabb.minY) / 2;
                    double scaleMax = 1 + 4 / 16D;
                    yOffset = scaleMin + value * (scaleMax - scaleMin);
                }
            }

            if (tile.clientDrillPos != null && tile.prevClientDrillPos != null) {
                Vec3 interpolatedPos = tile.prevClientDrillPos.add(
                        tile.clientDrillPos.subtract(tile.prevClientDrillPos).scale(partialTicks)
                );
                double frameY = max.getY() + 0.5;

                renderWorldLaser(poseStack, laserBuffer, cameraPos, new LaserData_BC8(
                        FRAME,
                        new Vec3(interpolatedPos.x + 0.5, frameY, interpolatedPos.z),
                        new Vec3(interpolatedPos.x + 0.5, frameY, max.getZ() + 12 / 16D),
                        1 / 16D, true, 0
                ));
                renderWorldLaser(poseStack, laserBuffer, cameraPos, new LaserData_BC8(
                        FRAME,
                        new Vec3(interpolatedPos.x + 0.5, frameY, interpolatedPos.z),
                        new Vec3(interpolatedPos.x + 0.5, frameY, min.getZ() + 4 / 16D),
                        1 / 16D, true, 0
                ));
                renderWorldLaser(poseStack, laserBuffer, cameraPos, new LaserData_BC8(
                        FRAME,
                        new Vec3(interpolatedPos.x, frameY, interpolatedPos.z + 0.5),
                        new Vec3(max.getX() + 12 / 16D, frameY, interpolatedPos.z + 0.5),
                        1 / 16D, true, 0
                ));
                renderWorldLaser(poseStack, laserBuffer, cameraPos, new LaserData_BC8(
                        FRAME,
                        new Vec3(interpolatedPos.x, frameY, interpolatedPos.z + 0.5),
                        new Vec3(min.getX() + 4 / 16D, frameY, interpolatedPos.z + 0.5),
                        1 / 16D, true, 0
                ));
                renderWorldLaser(poseStack, laserBuffer, cameraPos, new LaserData_BC8(
                        FRAME_BOTTOM,
                        new Vec3(interpolatedPos.x + 0.5, interpolatedPos.y + 1 + 4 / 16D, interpolatedPos.z + 0.5),
                        new Vec3(interpolatedPos.x + 0.5, frameY, interpolatedPos.z + 0.5),
                        1 / 16D, true, 0
                ));
                renderWorldLaser(poseStack, laserBuffer, cameraPos, new LaserData_BC8(
                        DRILL,
                        new Vec3(interpolatedPos.x + 0.5, interpolatedPos.y + 1 + yOffset, interpolatedPos.z + 0.5),
                        new Vec3(interpolatedPos.x + 0.5, interpolatedPos.y + yOffset, interpolatedPos.z + 0.5),
                        1 / 16D, true, 0
                ));
            } else {
                LaserBoxRenderer.renderLaserBoxWorld(
                        tile.frameBox, BuildCraftLaserManager.STRIPES_WRITE, poseStack.last(), laserBuffer, true, cameraPos
                );
            }
        } finally {
            profiler.pop();
            profiler.pop();
        }
    }

    private static void renderWorldLaser(
            PoseStack poseStack, VertexConsumer laserBuffer, Vec3 cameraPos, LaserData_BC8 laser
    ) {
        LaserRenderer_BC8.renderLaserWorld(laser, poseStack.last(), laserBuffer, cameraPos);
    }

    @Override
//    public boolean isGlobalRenderer(TileQuarry te)
    public boolean shouldRenderOffScreen(TileQuarry tile) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 512;
    }

    public static void init() {

    }
    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(TileQuarry tile) {
        return tile.getRenderBoundingBox();
    }

}
