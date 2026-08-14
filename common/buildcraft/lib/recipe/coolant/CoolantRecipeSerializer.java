package buildcraft.lib.recipe.coolant;

import buildcraft.api.fuels.EnumCoolantType;
import buildcraft.api.fuels.ICoolant;
import buildcraft.api.fuels.IFluidCoolant;
import buildcraft.api.fuels.ISolidCoolant;
import buildcraft.lib.misc.JsonUtil;
import buildcraft.lib.recipe.LegacyRecipeCodec;
import buildcraft.lib.recipe.coolant.CoolantRegistry.FluidCoolant;
import buildcraft.lib.recipe.coolant.CoolantRegistry.SolidCoolant;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class CoolantRecipeSerializer implements RecipeSerializer<ICoolant> {
    public static final CoolantRecipeSerializer INSTANCE = new CoolantRecipeSerializer();
    private static final MapCodec<ICoolant> CODEC = LegacyRecipeCodec.mapCodec(
            ICoolant.TYPE_ID, CoolantRecipeSerializer::fromJson, CoolantRecipeSerializer::toJson);
    private static final StreamCodec<RegistryFriendlyByteBuf, ICoolant> STREAM_CODEC = LegacyRecipeCodec.streamCodec(CODEC);

    private static ICoolant fromJson(ResourceLocation recipeId, JsonObject json) {
        EnumCoolantType type = EnumCoolantType.byName(GsonHelper.getAsString(json, "coolantType"));
        FluidStack fluid = JsonUtil.deSerializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
        return switch (type) {
            case FLUID -> new FluidCoolant(recipeId, fluid, GsonHelper.getAsFloat(json, "degreesCoolingPerMb"));
            case SOLID -> new SolidCoolant(recipeId, JsonUtil.deSerializeItemStack(GsonHelper.getAsJsonObject(json, "solid")), fluid,
                    GsonHelper.getAsFloat(json, "multiplier"));
        };
    }

    private static void toJson(ICoolant recipe, JsonObject json) {
        json.addProperty("id", recipe.getId().toString());
        json.addProperty("coolantType", recipe.getCoolantType().getLowerName());
        json.add("fluid", JsonUtil.serializeFluidStack(recipe.getFluid()));
        if (recipe instanceof ISolidCoolant solid) {
            json.addProperty("multiplier", solid.getMultiplier());
            json.add("solid", JsonUtil.serializeItemStack(solid.getSolid()));
        } else if (recipe instanceof IFluidCoolant fluid) {
            json.addProperty("degreesCoolingPerMb", fluid.getDegreesCoolingPerMB());
        }
    }

    public static void toJson(CoolantRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", ICoolant.TYPE_ID.toString());
        json.addProperty("coolantType", builder.type.getLowerName());
        json.add("fluid", JsonUtil.serializeFluidStack(builder.fluid));
        if (builder.type == EnumCoolantType.SOLID) {
            json.addProperty("multiplier", builder.multiplier);
            json.add("solid", JsonUtil.serializeItemStack(builder.solid));
        } else json.addProperty("degreesCoolingPerMb", builder.degreesCoolingPerMb);
    }

    @Override public MapCodec<ICoolant> codec() { return CODEC; }
    @Override public StreamCodec<RegistryFriendlyByteBuf, ICoolant> streamCodec() { return STREAM_CODEC; }
}
