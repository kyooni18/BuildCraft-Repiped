package buildcraft.lib.recipe.programming;

import buildcraft.api.recipes.IProgrammingRecipe;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ProgrammingRecipeSerializer implements RecipeSerializer<IProgrammingRecipe> {
    public static final ProgrammingRecipeSerializer INSTANCE = new ProgrammingRecipeSerializer();
    private static final MapCodec<IProgrammingRecipe> CODEC = LegacyRecipeCodec.mapCodec(
            IProgrammingRecipe.TYPE_ID, ProgrammingRecipeSerializer::fromJson, ProgrammingRecipeSerializer::toJson);
    private static final StreamCodec<RegistryFriendlyByteBuf, IProgrammingRecipe> STREAM_CODEC = LegacyRecipeCodec.streamCodec(CODEC);

    private static IProgrammingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        IngredientStack input = JsonUtil.deSerializeIngredientStack(GsonHelper.getAsJsonObject(json, "input"));
        ItemStack output = JsonUtil.deSerializeItemStack(GsonHelper.getAsJsonObject(json, "output"));
        long energyCost = GsonHelper.getAsLong(json, "energyCost");
        return new BoardProgrammingRecipe(recipeId, input, output, energyCost);
    }

    private static void toJson(IProgrammingRecipe recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        json.add("input", JsonUtil.serializeIngredientStack(recipe.getInput()));
        json.add("output", JsonUtil.serializeItemStack(recipe.getOutput()));
        json.addProperty("energyCost", recipe.getEnergyCost());
    }

    public static void toJson(ProgrammingRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", IProgrammingRecipe.TYPE_ID.toString());
        json.add("input", JsonUtil.serializeIngredientStack(builder.getInput()));
        json.add("output", JsonUtil.serializeItemStack(builder.getOutput()));
        json.addProperty("energyCost", builder.getEnergyCost());
    }

    @Override
    public MapCodec<IProgrammingRecipe> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, IProgrammingRecipe> streamCodec() { return STREAM_CODEC; }
}
