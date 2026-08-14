package buildcraft.lib.recipe.fuel;

import buildcraft.api.fuels.IFuel;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class FuelRecipeSerializer implements RecipeSerializer<IFuel> {
    public static final FuelRecipeSerializer INSTANCE = new FuelRecipeSerializer();
    private static final MapCodec<IFuel> CODEC = LegacyRecipeCodec.mapCodec(
            IFuel.TYPE_ID, FuelRecipeSerializer::fromJson, FuelRecipeSerializer::toJson);
    private static final StreamCodec<RegistryFriendlyByteBuf, IFuel> STREAM_CODEC = LegacyRecipeCodec.streamCodec(CODEC);

    private static IFuel fromJson(ResourceLocation recipeId, JsonObject json) {
        FluidStack fluid = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
        long powerPerCycle = GsonHelper.getAsLong(json, "powerPerCycle");
        int totalBurningTime = GsonHelper.getAsInt(json, "totalBurningTime");
        boolean dirty = GsonHelper.getAsBoolean(json, "dirty", false);
        if (dirty) {
            FluidStack residue = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "residue"));
            return new FuelRegistry.DirtyFuel(recipeId, fluid, powerPerCycle, totalBurningTime, residue);
        }
        return new FuelRegistry.Fuel(recipeId, fluid, powerPerCycle, totalBurningTime);
    }

    private static void toJson(IFuel recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        json.add("fluid", JsonUtil.serializeFluidStack(recipe.getFluid()));
        json.addProperty("powerPerCycle", recipe.getPowerPerCycle());
        json.addProperty("totalBurningTime", recipe.getTotalBurningTime());
        boolean dirty = recipe instanceof FuelRegistry.DirtyFuel;
        json.addProperty("dirty", dirty);
        if (dirty) json.add("residue", JsonUtil.serializeFluidStack(((FuelRegistry.DirtyFuel) recipe).getResidue()));
    }

    public static void toJson(FuelRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", IFuel.TYPE_ID.toString());
        json.add("fluid", JsonUtil.serializeFluidStack(builder.fluid));
        json.addProperty("powerPerCycle", builder.powerPerCycle);
        json.addProperty("totalBurningTime", builder.totalBurningTime);
        json.addProperty("dirty", builder.dirty);
        if (builder.dirty) json.add("residue", JsonUtil.serializeFluidStack(builder.residue));
    }

    @Override public MapCodec<IFuel> codec() { return CODEC; }
    @Override public StreamCodec<RegistryFriendlyByteBuf, IFuel> streamCodec() { return STREAM_CODEC; }
}
