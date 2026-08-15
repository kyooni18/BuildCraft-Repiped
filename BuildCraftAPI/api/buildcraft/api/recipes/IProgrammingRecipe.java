package buildcraft.api.recipes;

import buildcraft.api.BCModules;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

// public interface IProgrammingRecipe
public interface IProgrammingRecipe extends Recipe<RecipeInput> {
    public static final ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BCModules.SILICON.getModId(), "programming");

    public static final RecipeType<IProgrammingRecipe> TYPE = RecipeType.simple(TYPE_ID);

    @Override
    default RecipeType<IProgrammingRecipe> getType() {
        return TYPE;
    }

    ResourceLocation getId();

//    /** Get a list (size at least width*height) of ItemStacks representing options.
//     *
//     * @param width The width of the Programming Table panel.
//     * @param height The height of the Programming Table panel.
//     * @return */
//    List<ItemStack> getOptions(int width, int height);

    /** Get the energy cost of a given option ItemStack.
     *
     * @return */
    // int getEnergyCost(ItemStack option);
    long getEnergyCost();

    /** @param input The input stack.
     * @return Whether this recipe applies to the given input stack. */
    boolean canCraft(ItemStack input);

    /** Craft the input ItemStack with the given option into an output ItemStack.
     *
     * @param input
     * @return The output ItemStack. */
    // ItemStack craft(ItemStack input, ItemStack option);
    ItemStack craft(ItemStack input);

    IngredientStack getInput();

    ItemStack getOutput();

    // Recipe

    @Override
    public default boolean matches(RecipeInput inv, Level world) {
        return false;
    }

    @Override
    default ItemStack assemble(RecipeInput inv, HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    default ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public default boolean isSpecial() {
        return true;
    }
}
