package buildcraft.api.recipes;

import buildcraft.api.BCModules;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public abstract class IntegrationRecipe implements Recipe<RecipeInput> {
    public static final ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BCModules.SILICON.getModId(), "integration");

    public static final RecipeType<IntegrationRecipe> TYPE = RecipeType.simple(TYPE_ID);

    public final ResourceLocation name;

    private final long energyCost;
    private final int maxExpansionCount;

    public IntegrationRecipe(ResourceLocation name, long energyCost, int maxExpansionCount) {
        this.name = name;
        this.energyCost = energyCost;
        this.maxExpansionCount = maxExpansionCount;
    }

    /**
     * Determines the output of this recipe
     * @param target the stack in the middle to integrate the components into
     * @param toIntegrate All available stacks to integrate (not all have to be used up in this recipe)
     * @return The output to produce based on the inputs provided or an empty stack if the recipe isn't valid
     */
    public abstract ItemStack getOutput(@Nonnull ItemStack target, NonNullList<ItemStack> toIntegrate);

    // Calen 1.18.2
    public abstract ItemStack getExampleOutput();

    /**
     * Determines the components to use when crafting finishes
     * @return The components to use up
     */
    // public abstract ImmutableList<IngredientStack> getRequirements(@Nonnull ItemStack output);
    public abstract ImmutableList<IngredientStack> getRequirements();

    /**
     * Determines the amount of MJ required to integrate
     * @return The powercost in microjoules
     */
    // public abstract long getRequiredMicroJoules(ItemStack output);
    public final long getRequiredMicroJoules() {
        return energyCost;
    }

    public abstract IngredientStack getCenterStack();

    public int getMaxExpansionCount() {
        return maxExpansionCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntegrationRecipe that = (IntegrationRecipe) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    // Recipe

    public ResourceLocation getId() {
        return name;
    }

    @Override
    public boolean matches(RecipeInput inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput inv, HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeType<IntegrationRecipe> getType() {
        return TYPE;
    }
}
