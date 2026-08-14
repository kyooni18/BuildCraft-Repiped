package buildcraft.lib.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/** Compatibility codec for BuildCraft's pre-1.21 flat JSON recipe formats. */
public final class LegacyRecipeCodec {
    private LegacyRecipeCodec() {
    }

    public static <T> MapCodec<T> mapCodec(ResourceLocation serializerId,
                                            BiFunction<ResourceLocation, JsonObject, T> decoder,
                                            BiConsumer<T, JsonObject> encoder) {
        Codec<T> codec = Codec.PASSTHROUGH.flatXmap(dynamic -> {
            JsonElement element = dynamic.convert(JsonOps.INSTANCE).getValue();
            if (!element.isJsonObject()) {
                return DataResult.error(() -> "Expected a recipe object, got " + element);
            }
            JsonObject json = element.getAsJsonObject().deepCopy();
            if (!json.has("type")) {
                json.addProperty("type", serializerId.toString());
            }
            ResourceLocation recipeId = serializerId;
            if (json.has("id")) {
                recipeId = ResourceLocation.parse(json.get("id").getAsString());
            }
            try {
                return DataResult.success(decoder.apply(recipeId, json));
            } catch (RuntimeException ex) {
                return DataResult.error(() -> "Failed to decode BuildCraft recipe: " + ex.getMessage());
            }
        }, value -> {
            JsonObject json = new JsonObject();
            try {
                encoder.accept(value, json);
                // Recipe.CODEC owns the outer serializer discriminator.
                json.remove("type");
                return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json));
            } catch (RuntimeException ex) {
                return DataResult.error(() -> "Failed to encode BuildCraft recipe: " + ex.getMessage());
            }
        });
        return MapCodec.assumeMapUnsafe(codec);
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> streamCodec(MapCodec<T> codec) {
        return ByteBufCodecs.fromCodecWithRegistries(codec.codec());
    }
}
