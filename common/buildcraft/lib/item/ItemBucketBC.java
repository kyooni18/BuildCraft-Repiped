package buildcraft.lib.item;

import buildcraft.lib.registry.CreativeTabManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import buildcraft.api.compat.capability.ICapabilityProvider;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import buildcraft.api.compat.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ItemBucketBC extends BucketItem {
    public ItemBucketBC(Supplier<? extends Fluid> supplier, Properties properties) {
//        super(supplier, properties.tab(CreativeTabManager.getTab("vanilla.misc")));
        super(supplier.get(), properties);
        CreativeTabManager.addItem(CreativeTabManager.getTab("vanilla.tools_and_utilities"), this);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.buildcraft.bucket_filled", content.getFluidType().getDescription().getString());
    }


    // Calen 1.20.1
    public ResourceLocation getRegistryName() {
        return ForgeRegistries.ITEMS.getKey(this);
    }
}
