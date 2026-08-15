package buildcraft.lib.recipe;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public final class IngredientNBTBC {
    private IngredientNBTBC() {
    }

    public static Ingredient of(ItemStack stack) {
        return DataComponentIngredient.of(true, stack);
    }
}
