/** Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution. */
package buildcraft.api.boards;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class RedstoneBoardNBT<T> {

    private static Random rand = new Random();

    public abstract ResourceLocation getID();

    // public abstract void addInformation(ItemStack stack, Player player, List<String> list, boolean advanced);
    public abstract void addInformation(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag);

    // public abstract String getDisplayName();
    public final String getDisplayName() {
        return getDisplayNameComponent().getString();
    }

    public abstract Component getDisplayNameComponent();

    public abstract IRedstoneBoard<T> create(CompoundTag nbt, T object);

    // public abstract String getItemModelLocation();
    public abstract String getBoardTexture();

//    public void createBoard(CompoundTag nbt) {
//        nbt.putString("id", getID().toString());
//    }

    public int getParameterNumber(CompoundTag nbt) {
        if (!nbt.contains("parameters")) {
            return 0;
        } else {
            return nbt.getList("parameters", Tag.TAG_COMPOUND).size();
        }
    }

    public float nextFloat(int difficulty) {
        return 1F - (float) Math.pow(rand.nextFloat(), 1F / difficulty);
    }
}
