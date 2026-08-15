/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.client.render.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LaserCompiledBuffer {
    private static final int DOUBLE_STRIDE = 5;
    // private static final int INT_STRIDE = 2;
    private static final int INT_STRIDE = 3;
    private static final int NORMAL_STRIDE = 3;
    private final int vertices;
    private final double[] da;
    private final int[] ia;
    private final float[] normals;

    public LaserCompiledBuffer(int vertices, double[] da, int[] ia, float[] normals) {
        this.vertices = vertices;
        this.da = da;
        this.ia = ia;
        this.normals = normals;
        if (normals.length != vertices * NORMAL_STRIDE) {
            throw new IllegalArgumentException("Expected " + (vertices * NORMAL_STRIDE) + " normal values, got " + normals.length);
        }
    }

    /** Emits position, color, UV, overlay, light and normal data expected by the dynamic laser RenderType. */
    public void render(PoseStack.Pose pose, VertexConsumer buffer) {
        for (int i = 0; i < vertices; i++) {
            // POSITION_3F
//            buffer.pos(da[DOUBLE_STRIDE * i + 0], da[DOUBLE_STRIDE * i + 1], da[DOUBLE_STRIDE * i + 2]);
            buffer.addVertex(pose.pose(), (float) da[DOUBLE_STRIDE * i + 0], (float) da[DOUBLE_STRIDE * i + 1], (float) da[DOUBLE_STRIDE * i + 2]);

            // COLOR_4UB
            int c = ia[INT_STRIDE * i + 0];
            buffer.setColor(c & 0xFF, (c >> 8) & 0xFF, (c >> 16) & 0xFF, (c >> 24) & 0xFF);

            // TEX_2F
            buffer.setUv((float) da[DOUBLE_STRIDE * i + 3], (float) da[DOUBLE_STRIDE * i + 4]);

            // Calen Overlay
            buffer.setOverlay(ia[INT_STRIDE * i + 1]);

            // TEX_2S
            int lmap = ia[INT_STRIDE * i + 2];
//            buffer.lightmap((lmap >> 16) & 0xFFFF, lmap & 0xFFFF);
            buffer.setLight(lmap);

            buffer.setNormal(
                    pose,
                    normals[NORMAL_STRIDE * i],
                    normals[NORMAL_STRIDE * i + 1],
                    normals[NORMAL_STRIDE * i + 2]
            );
        }
    }

    public static class Builder implements ILaserRenderer {
        // private final boolean useNormalColour;
        private final DoubleArrayList doubleData = new DoubleArrayList();
        private final IntArrayList intData = new IntArrayList();
        private final FloatArrayList normalData = new FloatArrayList();
        private int vertices = 0;

        // public Builder(boolean useNormalColour)
        public Builder() {
//            this.useNormalColour = useNormalColour;
        }

        @Override
        public void vertex(
                double x, double y, double z,
                double u, double v,
                int lmap, int overlay,
                float nx, float ny, float nz,
                float diffuse
        ) {
            // POSITION_3F
            doubleData.add(x);
            doubleData.add(y);
            doubleData.add(z);

            // COLOR_4UB
//            if (useNormalColour) {
            int c = (int) (diffuse * 0xFF);
            intData.add(c | c << 8 | c << 16 | 0xFF << 24);
//            } else {
//                intData.add(0xFF_FF_FF_FF);
//            }

            // TEX_2F
            doubleData.add(u);
            doubleData.add(v);

            // OVERLAY
            intData.add(overlay);

            // TEX_2S
            intData.add(lmap);

            // NORMAL_3F
            normalData.add(nx);
            normalData.add(ny);
            normalData.add(nz);

            vertices++;
        }

        public LaserCompiledBuffer build() {
            return new LaserCompiledBuffer(vertices, doubleData.toDoubleArray(), intData.toIntArray(), normalData.toFloatArray());
        }
    }
}
