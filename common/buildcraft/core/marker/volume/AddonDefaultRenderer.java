/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.core.marker.volume;

import buildcraft.lib.client.sprite.White;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import buildcraft.api.compat.LazyOptional;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class AddonDefaultRenderer<T extends Addon> implements IFastAddonRenderer<T> {
    // private final TextureAtlasSprite s;
    private final LazyOptional<TextureAtlasSprite> s;

    public AddonDefaultRenderer() {
//        s = ModelLoader.White.INSTANCE;
        s = LazyOptional.of(White::instance);
    }

    public AddonDefaultRenderer(TextureAtlasSprite s) {
//        this.s = s;
        this.s = LazyOptional.of(() -> s);
    }

    @Override
//    public void renderAddonFast(T addon, Player player, float partialTicks, BufferBuilder builder)
    public void renderAddonFast(T addon, Player player, PoseStack.Pose pose, float partialTicks, VertexConsumer builder) {
        AABB bb = addon.getBoundingBox();

        Matrix4f posePose = pose.pose();
        Matrix3f normal = pose.normal();
        TextureAtlasSprite s = this.s.resolve().get();
        builder.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.minZ).setColor(204, 204, 204, 255).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ).setColor(204, 204, 204, 255).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.minZ).setColor(204, 204, 204, 255).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.minZ).setColor(204, 204, 204, 255).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

        builder.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.maxZ).setColor(204, 204, 204, 255).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ).setColor(204, 204, 204, 255).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ).setColor(204, 204, 204, 255).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ).setColor(204, 204, 204, 255).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

        builder.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.minZ).setColor(127, 127, 127, 255).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.minZ).setColor(127, 127, 127, 255).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ).setColor(127, 127, 127, 255).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.maxZ).setColor(127, 127, 127, 255).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

        builder.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ).setColor(255, 255, 255, 255).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ).setColor(255, 255, 255, 255).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ).setColor(255, 255, 255, 255).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.minZ).setColor(255, 255, 255, 255).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

        builder.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.maxZ).setColor(153, 153, 153, 255).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ).setColor(153, 153, 153, 255).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.maxY, (float) bb.minZ).setColor(153, 153, 153, 255).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.minX, (float) bb.minY, (float) bb.minZ).setColor(153, 153, 153, 255).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);

        builder.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.minZ).setColor(153, 153, 153, 255).setUv(s.getU0(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ).setColor(153, 153, 153, 255).setUv(s.getU0(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ).setColor(153, 153, 153, 255).setUv(s.getU1(), s.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
        builder.addVertex(posePose, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ).setColor(153, 153, 153, 255).setUv(s.getU1(), s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 0).setNormal(1, 1, 1);
    }
}
