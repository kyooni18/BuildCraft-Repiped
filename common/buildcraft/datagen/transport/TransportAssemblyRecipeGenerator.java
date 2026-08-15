package buildcraft.datagen.transport;

import buildcraft.api.mj.MjAPI;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.misc.ColourUtil;
import buildcraft.lib.recipe.assembly.AssemblyRecipeBuilder;
import buildcraft.transport.BCTransport;
import buildcraft.transport.BCTransportItems;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import buildcraft.lib.recipe.FinishedRecipe;
import buildcraft.datagen.base.BCCompatRecipeProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TransportAssemblyRecipeGenerator extends BCCompatRecipeProvider {
    public TransportAssemblyRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(BCRecipeOutput consumer) {
        if (BCTransportItems.wire != null) {
            for (DyeColor color : ColourUtil.COLOURS) {
//                String name = String.format("wire-%s", color.getUnlocalizedName());
                String name = String.format("wire_%s", color.getSerializedName());
//                ImmutableSet<IngredientStack> input = ImmutableSet.of(IngredientStack.of(Tags.Items.DUSTS_REDSTONE), IngredientStack.of(ColourUtil.getDyeName(color)));
                ImmutableSet<IngredientStack> input = ImmutableSet.of(IngredientStack.of(Tags.Items.DUSTS_REDSTONE), IngredientStack.of(color.getTag()));
//                AssemblyRecipeRegistry.register(new AssemblyRecipeBasic(name, 10_000 * MjAPI.MJ, input, new ItemStack(BCTransportItems.wire, 8, color.getMetadata())));
                ItemStack wireStack = new ItemStack(BCTransportItems.wire.get(), 8);
                ColourUtil.addColourTagToStack(wireStack, color);
                AssemblyRecipeBuilder.basic(10_000 * MjAPI.MJ, input, wireStack).save(consumer, BCTransport.MODID, name);
            }
        }
    }
}
