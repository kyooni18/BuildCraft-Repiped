/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 */
package buildcraft.lib.client.render.fluid;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;

/** Creates the repeated 2x2 image used by BuildCraft's frozen-fluid renderer.
 *
 * NeoForge 1.21 no longer exposes the old custom TextureAtlasSprite loader hook. The atlas therefore contains a
 * normal TextureAtlasSprite and {@link FluidRenderer} applies the historical frozen UV transform while rendering. */
public final class SpriteFluidFrozen {
    private SpriteFluidFrozen() {
    }

    public static SpriteContents createSpriteContents(TextureAtlasSprite src, ResourceLocation frozenLocation) {
        int widthOld = src.contents().width();
        int heightOld = src.contents().height();
        int width = widthOld * 2;
        int height = heightOld * 2;

        NativeImage source = src.contents().getOriginalImage();
        NativeImage frozen = new NativeImage(width, height, false);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                frozen.setPixelRGBA(x, y, source.getPixelRGBA(x % widthOld, y % heightOld));
            }
        }
        return new SpriteContents(frozenLocation, new FrameSize(width, height), frozen, ResourceMetadata.EMPTY);
    }
}
