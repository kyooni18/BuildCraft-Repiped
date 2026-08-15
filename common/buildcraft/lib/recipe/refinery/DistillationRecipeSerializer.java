package buildcraft.lib.recipe.refinery;

import buildcraft.api.recipes.IRefineryRecipeManager.IDistillationRecipe;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

public class DistillationRecipeSerializer implements RecipeSerializer<IDistillationRecipe> {
    public static final DistillationRecipeSerializer INSTANCE = new DistillationRecipeSerializer();
    private static final MapCodec<IDistillationRecipe> CODEC = LegacyRecipeCodec.mapCodec(
            IDistillationRecipe.TYPE_ID, DistillationRecipeSerializer::fromJson, DistillationRecipeSerializer::toJson);
    private static final StreamCodec<RegistryFriendlyByteBuf, IDistillationRecipe> STREAM_CODEC = LegacyRecipeCodec.streamCodec(CODEC);

    private static IDistillationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        long powerRequired = GsonHelper.getAsLong(json, "powerRequired");
        FluidStack in = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "in"));
        FluidStack outGas = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "outGas"));
        FluidStack outLiquid = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "outLiquid"));
        return new RefineryRecipeRegistry.DistillationRecipe(recipeId, powerRequired, in, outGas, outLiquid);
    }

    private static void toJson(IDistillationRecipe recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        json.addProperty("powerRequired", recipe.powerRequired());
        json.add("in", JsonUtil.serializeFluidStack(recipe.in()));
        json.add("outGas", JsonUtil.serializeFluidStack(recipe.outGas()));
        json.add("outLiquid", JsonUtil.serializeFluidStack(recipe.outLiquid()));
    }

    public static void toJson(DistillationRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", IDistillationRecipe.TYPE_ID.toString());
        json.addProperty("powerRequired", builder.powerRequired);
        json.add("in", JsonUtil.serializeFluidStack(builder.in));
        json.add("outGas", JsonUtil.serializeFluidStack(builder.outGas));
        json.add("outLiquid", JsonUtil.serializeFluidStack(builder.outLiquid));
    }

    @Override public MapCodec<IDistillationRecipe> codec() { return CODEC; }
    @Override public StreamCodec<RegistryFriendlyByteBuf, IDistillationRecipe> streamCodec() { return STREAM_CODEC; }
}
