package buildcraft.datagen.robotics;

import buildcraft.api.boards.RedstoneBoardNBT;
import buildcraft.api.boards.RedstoneBoardRegistry;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.recipe.programming.ProgrammingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import buildcraft.lib.recipe.FinishedRecipe;
import buildcraft.datagen.base.BCCompatRecipeProvider;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RoboticsProgrammingRecipeGenerator extends BCCompatRecipeProvider {
    public RoboticsProgrammingRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(BCRecipeOutput consumer) {
        IngredientStack input = IngredientStack.of(new ItemStack(RedstoneBoardRegistry.instance.getBoardNBTItemMap().get(RedstoneBoardRegistry.instance.getEmptyRobotBoard()).get()));
        for (RedstoneBoardNBT<?> boardNBT : RedstoneBoardRegistry.instance.getAllBoardNBTs()) {
            ItemStack output = RedstoneBoardRegistry.instance.getBoardNBTItemMap().get(boardNBT).get().getDefaultInstance();
            ProgrammingRecipeBuilder.programming(input, output, RedstoneBoardRegistry.instance.getPowerCost(boardNBT)).save(consumer, boardNBT.getID().getPath());
        }
    }
}
