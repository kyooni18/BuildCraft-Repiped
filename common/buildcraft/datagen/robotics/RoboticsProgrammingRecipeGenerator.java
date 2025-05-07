package buildcraft.datagen.robotics;

import buildcraft.api.boards.RedstoneBoardNBT;
import buildcraft.api.boards.RedstoneBoardRegistry;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.recipe.programming.ProgrammingRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class RoboticsProgrammingRecipeGenerator extends RecipeProvider {
    public RoboticsProgrammingRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        IngredientStack input = IngredientStack.of(new ItemStack(RedstoneBoardRegistry.instance.getBoardNBTItemMap().get(RedstoneBoardRegistry.instance.getEmptyRobotBoard()).get()));
        for (RedstoneBoardNBT<?> boardNBT : RedstoneBoardRegistry.instance.getAllBoardNBTs()) {
            ItemStack output = RedstoneBoardRegistry.instance.getBoardNBTItemMap().get(boardNBT).get().getDefaultInstance();
            ProgrammingRecipeBuilder.programming(input, output, RedstoneBoardRegistry.instance.getPowerCost(boardNBT)).save(consumer, boardNBT.getID().getPath());
        }
    }

    @Override
    public String getName() {
        return "BuildCraft Programming Integration Recipe Generator";
    }
}
