package buildcraft.datagen.lib;

import buildcraft.lib.BCLib;
import buildcraft.lib.BCLibItems;
import buildcraft.lib.oredictionarytag.OreDictionaryTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import buildcraft.lib.recipe.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import buildcraft.datagen.base.BCCompatRecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class LibCraftingRecipeGenerator extends BCCompatRecipeProvider {
    private static final String MOD_ID = BCLib.MODID;

    public LibCraftingRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(BCRecipeOutput consumer) {
        // guide
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BCLibItems.guide.get())
                .requires(OreDictionaryTags.GEAR_WOOD)
                .requires(Items.PAPER)
                .requires(Items.PAPER)
                .requires(Items.PAPER)
                .unlockedBy("has_item", has(OreDictionaryTags.WORKBENCHES_ITEM))
                .group(MOD_ID)
                .save(consumer);
    }
}
