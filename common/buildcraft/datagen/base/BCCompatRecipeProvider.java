package buildcraft.datagen.base;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import buildcraft.lib.recipe.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Bridges BuildCraft's legacy custom recipe builders with Minecraft 1.21's RecipeOutput.
 * Vanilla recipe builders are encoded through Recipe.CODEC, while BuildCraft custom
 * recipes keep their existing JSON serializers until their runtime codecs are invoked.
 */
public abstract class BCCompatRecipeProvider extends RecipeProvider {
    protected BCCompatRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected final CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
        Set<ResourceLocation> ids = Sets.newHashSet();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        BCRecipeOutput output = new BCRecipeOutput() {
            private void checkDuplicate(ResourceLocation id) {
                if (!ids.add(id)) {
                    throw new IllegalStateException("Duplicate recipe " + id);
                }
            }

            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
                checkDuplicate(id);
                futures.add(DataProvider.saveStable(
                        cache, registries, Recipe.CONDITIONAL_CODEC,
                        Optional.of(new WithConditions<>(recipe, conditions)),
                        recipePathProvider.json(id)
                ));
                if (advancement != null) {
                    futures.add(DataProvider.saveStable(
                            cache, registries, Advancement.CONDITIONAL_CODEC,
                            Optional.of(new WithConditions<>(advancement.value(), conditions)),
                            advancementPathProvider.json(advancement.id())
                    ));
                }
            }

            @Override
            public void accept(FinishedRecipe finished) {
                ResourceLocation id = finished.getId();
                checkDuplicate(id);

                ResourceLocation serializerId = BuiltInRegistries.RECIPE_SERIALIZER.getKey(finished.getType());
                if (serializerId == null) {
                    throw new IllegalStateException("Unregistered recipe serializer for " + id + ": " + finished.getType());
                }

                JsonObject json = new JsonObject();
                json.addProperty("type", serializerId.toString());
                json.addProperty("id", id.toString());
                finished.serializeRecipeData(json);
                futures.add(DataProvider.saveStable(cache, json, recipePathProvider.json(id)));

                JsonObject advancement = finished.serializeAdvancement();
                ResourceLocation advancementId = finished.getAdvancementId();
                if (advancement != null && advancementId != null) {
                    futures.add(DataProvider.saveStable(cache, advancement, advancementPathProvider.json(advancementId)));
                }
            }

            @Override
            public Advancement.Builder advancement() {
                return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
            }

        };

        buildRecipes(output);
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    protected final void buildRecipes(RecipeOutput output) {
        // RecipeProvider.run is overridden above and always supplies BCRecipeOutput.
        // Keeping this bridge makes the 1.21 abstract contract explicit.
        buildRecipes((BCRecipeOutput) output);
    }

    protected abstract void buildRecipes(BCRecipeOutput output);

    protected interface BCRecipeOutput extends RecipeOutput, Consumer<FinishedRecipe> {
    }
}
