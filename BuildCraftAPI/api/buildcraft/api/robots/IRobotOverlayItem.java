package buildcraft.api.robots;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IRobotOverlayItem {
    boolean isValidRobotOverlay(ItemStack stack);

    @OnlyIn(Dist.CLIENT)
    // void renderRobotOverlay(ItemStack stack, TextureManager textureManager);
    void renderRobotOverlay(ItemStack stack);
}
