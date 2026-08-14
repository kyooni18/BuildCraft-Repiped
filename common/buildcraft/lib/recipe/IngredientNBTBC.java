package buildcraft.lib.recipe;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.ingredients.StrictNBTIngredient;

public final class IngredientNBTBC {
    private IngredientNBTBC() {
    }

    public static Ingredient of(ItemStack stack) {
        return StrictNBTIngredient.of(stack);
    }
}
