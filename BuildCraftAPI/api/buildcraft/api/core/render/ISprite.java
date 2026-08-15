package buildcraft.api.core.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/** Holds information on some sort of sprite. These might not be part of the main texture atlas (they might be from a
 * GUI texture), in which case {@link #bindTexture()} should be called before using the results from
 * {@link #getInterpU(double)} or {@link #getInterpV(double)}
 * <p>
 * <b>IMPORTANT: This API uses normalized co-ordinates between 0 and 1. Minecraft 1.21's
 * {@link TextureAtlasSprite#getU(float)} and {@link TextureAtlasSprite#getV(float)} now use the same normalized range.</b> */
public interface ISprite {
    /** Binds this sprites backing texture so that this sprite will be referenced when you use the results of
     * {@link #getInterpU(double)} and {@link #getInterpV(double)} */
    void bindTexture();

    /** @param u A value between 0 and 1
     * @return */
    double getInterpU(double u);

    /** @param v A value between 0 and 1
     * @return */
    double getInterpV(double v);
}
