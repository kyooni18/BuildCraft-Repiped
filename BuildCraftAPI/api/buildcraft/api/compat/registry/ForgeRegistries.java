package buildcraft.api.compat.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/** Small Forge-era registry compatibility view backed by the vanilla 1.21 registries. */
public final class ForgeRegistries {
    private ForgeRegistries() {}

    public static final LegacyRegistry<Block> BLOCKS = new LegacyRegistry<>(BuiltInRegistries.BLOCK);
    public static final LegacyRegistry<Item> ITEMS = new LegacyRegistry<>(BuiltInRegistries.ITEM);
    public static final LegacyRegistry<Fluid> FLUIDS = new LegacyRegistry<>(BuiltInRegistries.FLUID);
    public static final LegacyRegistry<EntityType<?>> ENTITY_TYPES = new LegacyRegistry<>(BuiltInRegistries.ENTITY_TYPE);
    public static final LegacyRegistry<MenuType<?>> MENU_TYPES = new LegacyRegistry<>(BuiltInRegistries.MENU);
    public static final LegacyRegistry<RecipeType<?>> RECIPE_TYPES = new LegacyRegistry<>(BuiltInRegistries.RECIPE_TYPE);
    public static final LegacyRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = new LegacyRegistry<>(BuiltInRegistries.RECIPE_SERIALIZER);
    public static final LegacyBiomeRegistry BIOMES = new LegacyBiomeRegistry();
}
