/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.builders.addon;

import buildcraft.core.marker.volume.IFastAddonRenderer;
import buildcraft.lib.client.sprite.White;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddonRendererFillerPlanner implements IFastAddonRenderer<AddonFillerPlanner> {
    @Override
//    public void renderAddonFast(AddonFillerPlanner addon, Player player, float partialTicks, BufferBuilder vb)
    public void renderAddonFast(AddonFillerPlanner addon, Player player, PoseStack.Pose pose, float partialTicks, VertexConsumer vb) {
        if (addon.buildingInfo == null) {
            return;
        }
        Minecraft.getInstance().getProfiler().push("filler_planner");

        Minecraft.getInstance().getProfiler().push("iter");
        List<BlockPos> list = StreamSupport.stream(
//                        BlockPos.getAllInBoxMutable(addon.buildingInfo.box.min(), addon.buildingInfo.box.max()).spliterator(),
                        BlockPos.betweenClosed(addon.buildingInfo.box.min(), addon.buildingInfo.box.max()).spliterator(),
                        false
                )
                .filter(blockPos ->
                        addon.buildingInfo.getSnapshot().data.get(
                                addon.buildingInfo.getSnapshot().posToIndex(
                                        addon.buildingInfo.fromWorld(blockPos)
                                )
                        )
                )
//                .filter(player.level::isAirBlock)
                .filter(player.level()::isEmptyBlock)
//                .map(BlockPos.MutableBlockPos::toImmutable)
//                .map(BlockPos.MutableBlockPos::immutable) // Calen: betweenClosed ret mutable BlockPos
                .collect(Collectors.toCollection(ArrayList::new));
        Minecraft.getInstance().getProfiler().pop();

        Minecraft.getInstance().getProfiler().push("sort");
//        list.sort(Comparator.<BlockPos>comparingDouble(p -> player.getPositionVector().squareDistanceTo(new Vec3(p))).reversed());
        list.sort(Comparator.<BlockPos>comparingDouble(p -> player.position().distanceToSqr(Vec3.atLowerCornerOf(p))).reversed());
        Minecraft.getInstance().getProfiler().pop();

        Minecraft.getInstance().getProfiler().push("render");
        Matrix4f posePose = pose.pose();
        Matrix3f normal = pose.normal();
        for (BlockPos p : list) {
            AABB bb = new AABB(Vec3.atLowerCornerOf(p), Vec3.atLowerCornerOf(p.offset(1, 1, 1))).inflate(-0.1);
//            TextureAtlasSprite s = ModelLoader.White.INSTANCE;
            TextureAtlasSprite s = White.instance();

            vb.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.minZ).setColor(204, 204, 204, 127).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ).setColor(204, 204, 204, 127).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.minZ).setColor(204, 204, 204, 127).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.minZ).setColor(204, 204, 204, 127).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

            vb.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.maxZ).setColor(204, 204, 204, 127).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ).setColor(204, 204, 204, 127).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ).setColor(204, 204, 204, 127).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ).setColor(204, 204, 204, 127).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

            vb.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.minZ).setColor(127, 127, 127, 127).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.minZ).setColor(127, 127, 127, 127).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ).setColor(127, 127, 127, 127).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.maxZ).setColor(127, 127, 127, 127).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

            vb.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ).setColor(255, 255, 255, 127).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ).setColor(255, 255, 255, 127).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ).setColor(255, 255, 255, 127).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.minZ).setColor(255, 255, 255, 127).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

            vb.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.maxZ).setColor(153, 153, 153, 127).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ).setColor(153, 153, 153, 127).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.minZ).setColor(153, 153, 153, 127).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.minZ).setColor(153, 153, 153, 127).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

            vb.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.minZ).setColor(153, 153, 153, 127).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ).setColor(153, 153, 153, 127).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ).setColor(153, 153, 153, 127).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
            vb.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ).setColor(153, 153, 153, 127).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        }
        Minecraft.getInstance().getProfiler().pop();

        Minecraft.getInstance().getProfiler().pop();
    }
}
