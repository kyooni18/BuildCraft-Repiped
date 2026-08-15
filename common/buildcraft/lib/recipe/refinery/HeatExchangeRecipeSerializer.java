package buildcraft.lib.recipe.refinery;

import buildcraft.api.recipes.IRefineryRecipeManager.ICoolableRecipe;
import buildcraft.api.recipes.IRefineryRecipeManager.IHeatableRecipe;
import buildcraft.api.recipes.IRefineryRecipeManager.IHeatExchangerRecipe;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import buildcraft.lib.recipe.refinery.RefineryRecipeRegistry.CoolableRecipe;
import buildcraft.lib.recipe.refinery.RefineryRecipeRegistry.HeatableRecipe;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

public class HeatExchangeRecipeSerializer implements RecipeSerializer<IHeatExchangerRecipe> {
    public static final HeatExchangeRecipeSerializer HEATABLE = new HeatExchangeRecipeSerializer(EnumHeatExchangeRecipeType.HEATABLE);
    public static final HeatExchangeRecipeSerializer COOLABLE = new HeatExchangeRecipeSerializer(EnumHeatExchangeRecipeType.COOLABLE);

    private final EnumHeatExchangeRecipeType type;
    private final MapCodec<IHeatExchangerRecipe> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, IHeatExchangerRecipe> streamCodec;

    private HeatExchangeRecipeSerializer(EnumHeatExchangeRecipeType type) {
        this.type = type;
        ResourceLocation id = type == EnumHeatExchangeRecipeType.HEATABLE ? IHeatableRecipe.TYPE_ID : ICoolableRecipe.TYPE_ID;
        this.codec = LegacyRecipeCodec.mapCodec(id, this::fromJson, HeatExchangeRecipeSerializer::toJson);
        this.streamCodec = LegacyRecipeCodec.streamCodec(codec);
    }

    private IHeatExchangerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        FluidStack in = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "in"));
        FluidStack out = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "out"));
        int heatFrom = GsonHelper.getAsInt(json, "heatFrom");
        int heatTo = GsonHelper.getAsInt(json, "heatTo");
        return type == EnumHeatExchangeRecipeType.COOLABLE
                ? new CoolableRecipe(recipeId, in, out, heatFrom, heatTo)
                : new HeatableRecipe(recipeId, in, out, heatFrom, heatTo);
    }

    private static void toJson(IHeatExchangerRecipe recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        json.add("in", JsonUtil.serializeFluidStack(recipe.in()));
        json.add("out", JsonUtil.serializeFluidStack(recipe.out()));
        json.addProperty("heatFrom", recipe.heatFrom());
        json.addProperty("heatTo", recipe.heatTo());
    }

    public static void toJson(HeatExchangeRecipeBuilder builder, JsonObject json) {
        ResourceLocation id = builder.type == EnumHeatExchangeRecipeType.HEATABLE ? IHeatableRecipe.TYPE_ID : ICoolableRecipe.TYPE_ID;
        json.addProperty("type", id.toString());
        json.add("in", JsonUtil.serializeFluidStack(builder.in));
        json.add("out", JsonUtil.serializeFluidStack(builder.out));
        json.addProperty("heatFrom", builder.heatFrom);
        json.addProperty("heatTo", builder.heatTo);
    }

    @Override public MapCodec<IHeatExchangerRecipe> codec() { return codec; }
    @Override public StreamCodec<RegistryFriendlyByteBuf, IHeatExchangerRecipe> streamCodec() { return streamCodec; }
}
