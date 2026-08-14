package buildcraft.lib.recipe.assembly;

import buildcraft.api.recipes.EnumAssemblyRecipeType;
import buildcraft.api.recipes.IAssemblyRecipe;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import com.google.common.collect.ImmutableSet;
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

public class AssemblyRecipeSerializer implements RecipeSerializer<IAssemblyRecipe> {
    public static final AssemblyRecipeSerializer INSTANCE = new AssemblyRecipeSerializer();

    private static final MapCodec<IAssemblyRecipe> CODEC = LegacyRecipeCodec.mapCodec(
            AssemblyRecipe.TYPE_ID, AssemblyRecipeSerializer::fromJson, AssemblyRecipeSerializer::toJson);
    private static final StreamCodec<RegistryFriendlyByteBuf, IAssemblyRecipe> STREAM_CODEC = LegacyRecipeCodec.streamCodec(CODEC);

    private static IAssemblyRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        EnumAssemblyRecipeType subType = EnumAssemblyRecipeType.valueOf(GsonHelper.getAsString(json, "subType"));
        return switch (subType) {
            case BASIC -> {
                long requiredMicroJoules = GsonHelper.getAsLong(json, "requiredMicroJoules");
                List<IngredientStack> requiredStacks = Lists.newArrayList();
                GsonHelper.getAsJsonArray(json, "requiredStacks").forEach(j -> requiredStacks.add(JsonUtil.deSerializeIngredientStack(j.getAsJsonObject())));
                ItemStack output = JsonUtil.deSerializeItemStack(GsonHelper.getAsJsonObject(json, "output"));
                yield new AssemblyRecipeBasic(recipeId, requiredMicroJoules, ImmutableSet.copyOf(requiredStacks), output);
            }
            case FACADE -> AssemblyRecipeRegistry.FACADE_ASSEMBLY_RECIPE;
        };
    }

    private static void toJson(IAssemblyRecipe recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        if (recipe instanceof IFacadeAssemblyRecipes) {
            json.addProperty("subType", EnumAssemblyRecipeType.FACADE.name());
            return;
        }
        json.addProperty("subType", EnumAssemblyRecipeType.BASIC.name());
        json.addProperty("requiredMicroJoules", recipe.getRequiredMicroJoules());
        JsonArray requiredStacks = new JsonArray();
        recipe.getRequiredIngredientStacks().forEach(stack -> requiredStacks.add(JsonUtil.serializeIngredientStack(stack)));
        json.add("requiredStacks", requiredStacks);
        ItemStack output = recipe.getOutput().stream().findFirst().orElse(ItemStack.EMPTY);
        json.add("output", JsonUtil.serializeItemStack(output));
    }

    public static void toJson(AssemblyRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", AssemblyRecipe.TYPE_ID.toString());
        json.addProperty("subType", builder.type.name());
        if (builder.type == EnumAssemblyRecipeType.BASIC) {
            json.addProperty("requiredMicroJoules", builder.requiredMicroJoules);
            JsonArray requiredStacks = new JsonArray();
            builder.requiredStacks.forEach(stack -> requiredStacks.add(JsonUtil.serializeIngredientStack(stack)));
            json.add("requiredStacks", requiredStacks);
            json.add("output", JsonUtil.serializeItemStack(builder.output));
        }
    }

    @Override
    public MapCodec<IAssemblyRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, IAssemblyRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
