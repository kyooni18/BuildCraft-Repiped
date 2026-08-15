/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.api.recipes;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public final class IngredientStack {
    public final Ingredient ingredient;
    public final int count;

    public IngredientStack(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public IngredientStack(Ingredient ingredient) {
        this(ingredient, 1);
    }

    // public static IngredientStack of(Object o)
    public static IngredientStack of(JsonElement o) {
        return new IngredientStack(Ingredient.CODEC.parse(JsonOps.INSTANCE, o).getOrThrow());
    }

    public static IngredientStack of(ItemStack o) {
        return new IngredientStack(Ingredient.of(o));
    }

    public static IngredientStack of(ItemLike o) {
        return new IngredientStack(Ingredient.of(o));
    }

    public static IngredientStack of(TagKey tag) {
        return new IngredientStack(Ingredient.of(tag));
    }

    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.count);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, this.ingredient);
    }

    public static IngredientStack fromNetwork(RegistryFriendlyByteBuf buffer) {
        int countIn = buffer.readInt();
        Ingredient ingredientIn = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        return new IngredientStack(ingredientIn, countIn);
    }
}
