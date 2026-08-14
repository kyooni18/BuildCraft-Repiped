package buildcraft.lib.recipe.integration;

import buildcraft.api.BCModules;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.api.recipes.IntegrationRecipe;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class IntegrationRecipeSerializer implements RecipeSerializer<IntegrationRecipe> {
    public static final IntegrationRecipeSerializer INSTANCE = new IntegrationRecipeSerializer();
    private static final MapCodec<IntegrationRecipe> CODEC = LegacyRecipeCodec.mapCodec(
            IntegrationRecipe.TYPE_ID, IntegrationRecipeSerializer::fromJson, IntegrationRecipeSerializer::toJson);
    private static final StreamCodec<RegistryFriendlyByteBuf, IntegrationRecipe> STREAM_CODEC = LegacyRecipeCodec.streamCodec(CODEC);

    private IntegrationRecipeSerializer() {
    }

    private static IntegrationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        long requiredMicroJoules = GsonHelper.getAsLong(json, "requiredMicroJoules");
        IngredientStack centerStack = JsonUtil.deSerializeIngredientStack(GsonHelper.getAsJsonObject(json, "centerStack"));
        List<IngredientStack> requirements = Lists.newArrayList();
        GsonHelper.getAsJsonArray(json, "requirements").forEach(j -> requirements.add(JsonUtil.deSerializeIngredientStack(j.getAsJsonObject())));
        ItemStack output = JsonUtil.deSerializeItemStack(GsonHelper.getAsJsonObject(json, "output"));
        return new IntegrationRecipeBasic(recipeId, requiredMicroJoules, centerStack, ImmutableList.copyOf(requirements), output);
    }

    private static void toJson(IntegrationRecipe recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        json.addProperty("requiredMicroJoules", recipe.getRequiredMicroJoules());
        json.add("centerStack", JsonUtil.serializeIngredientStack(recipe.getCenterStack()));
        JsonArray requirements = new JsonArray();
        recipe.getRequirements().forEach(stack -> requirements.add(JsonUtil.serializeIngredientStack(stack)));
        json.add("requirements", requirements);
        json.add("output", JsonUtil.serializeItemStack(recipe.getExampleOutput()));
    }

    public static void toJson(IntegrationRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", BCModules.SILICON.getModId() + ":integration");
        json.addProperty("requiredMicroJoules", builder.requiredMicroJoules);
        json.add("centerStack", JsonUtil.serializeIngredientStack(builder.centerStack));
        JsonArray requirements = new JsonArray();
        builder.requirements.forEach(stack -> requirements.add(JsonUtil.serializeIngredientStack(stack)));
        json.add("requirements", requirements);
        json.add("output", JsonUtil.serializeItemStack(builder.exampleOutput));
    }

    @Override
    public MapCodec<IntegrationRecipe> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, IntegrationRecipe> streamCodec() { return STREAM_CODEC; }
}
