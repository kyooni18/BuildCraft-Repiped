package buildcraft.lib.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

public interface FinishedRecipe {
    void serializeRecipeData(JsonObject json);

    ResourceLocation getId();

    RecipeSerializer<?> getType();

    @Nullable
    JsonObject serializeAdvancement();

    @Nullable
    ResourceLocation getAdvancementId();
}
