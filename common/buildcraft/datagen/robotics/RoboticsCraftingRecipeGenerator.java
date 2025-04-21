package buildcraft.datagen.robotics;

import buildcraft.api.boards.RedstoneBoardRegistry;
import buildcraft.factory.BCFactory;
import buildcraft.lib.oredictionarytag.OreDictionaryTags;
import buildcraft.robotics.BCRoboticsBlocks;
import buildcraft.robotics.BCRoboticsItems;
import buildcraft.silicon.BCSiliconItems;
import buildcraft.transport.BCTransportItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RoboticsCraftingRecipeGenerator extends RecipeProvider {
    private static final String MOD_ID = BCFactory.MODID;

    public RoboticsCraftingRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // zonePlanner
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCRoboticsBlocks.zonePlanner.get())
                .pattern("wdw")
                .pattern("wcw")
                .pattern("wpw")
                .define('d', (Item) BCTransportItems.pipeItemDiamond.get(null).get())
                .define('w', ItemTags.PLANKS)
                .define('p', Blocks.PISTON)
                .define('c', Blocks.CHEST)
                .unlockedBy("has_item", has(OreDictionaryTags.GEAR_STONE))
                .group(MOD_ID)
                .save(consumer);
        // requester
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCRoboticsBlocks.requester.get())
                .pattern("IPI")
                .pattern("GCG")
                .pattern("IRI")
                .define('C', Tags.Items.CHESTS_WOODEN)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('P', Blocks.PISTON)
                .define('G', OreDictionaryTags.GEAR_IRON)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_item", has(OreDictionaryTags.GEAR_IRON))
                .group(MOD_ID)
                .save(consumer);
        // robot
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCRoboticsItems.robot.get(RedstoneBoardRegistry.instance.getEmptyRobotBoard()).get())
                .pattern("PPP")
                .pattern("PRP")
                .pattern("C C")
                .define('P', Tags.Items.INGOTS_IRON)
                .define('R', BCSiliconItems.redstoneCrystal.get())
                .define('C', BCSiliconItems.chipsetDiamond.get())
                .unlockedBy("has_item", has(BCSiliconItems.redstoneCrystal.get()))
                .group(MOD_ID)
                .save(consumer);
        // redstoneBoard
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCRoboticsItems.redstoneBoard.get(RedstoneBoardRegistry.instance.getEmptyRobotBoard()).get())
                .pattern("PPP")
                .pattern("PRP")
                .pattern("PPP")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('P', Items.PAPER)
                .unlockedBy("has_item", has(Tags.Items.DUSTS_REDSTONE))
                .group(MOD_ID)
                .save(consumer);
        // robotStation
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCRoboticsItems.robotStation.get())
                .pattern(" I ")
                .pattern("ICI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', BCSiliconItems.chipsetGold.get())
                .unlockedBy("has_item", has(BCSiliconItems.chipsetGold.get()))
                .group(MOD_ID)
                .save(consumer);
    }

    @Override
    public String getName() {
        return "BuildCraft Robotics Crafting Recipe Generator";
    }
}
