package buildcraft.datagen.silicon;

import buildcraft.silicon.recipe.FacadeSwapRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import buildcraft.lib.recipe.FinishedRecipe;
import buildcraft.datagen.base.BCCompatRecipeProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SiliconFacadeSwapRecipeGenerator extends BCCompatRecipeProvider {
    public SiliconFacadeSwapRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(BCRecipeOutput consumer) {
        // Facade swap
        FacadeSwapRecipeBuilder.swap("facade_swap").save(consumer);
    }
}
