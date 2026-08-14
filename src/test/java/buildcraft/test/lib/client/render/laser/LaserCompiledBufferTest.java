/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 */
package buildcraft.test.lib.client.render.laser;

import buildcraft.lib.client.render.laser.LaserCompiledBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class LaserCompiledBufferTest {
    @Test
    public void builderPreservesVertexNormals() throws Exception {
        LaserCompiledBuffer.Builder builder = new LaserCompiledBuffer.Builder();
        builder.vertex(1, 2, 3, 0.25, 0.75, 0x00F000F0, 0, 0.25f, -0.5f, 0.75f, 1.0f);
        LaserCompiledBuffer compiled = builder.build();

        Field normalsField = LaserCompiledBuffer.class.getDeclaredField("normals");
        normalsField.setAccessible(true);
        float[] normals = (float[]) normalsField.get(compiled);
        Assert.assertArrayEquals(new float[] { 0.25f, -0.5f, 0.75f }, normals, 0.0f);
    }
}
