package buildcraft.factory.loot;

import buildcraft.factory.BCFactory;
import buildcraft.factory.block.BlockWaterGel;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootConditionSpreading implements LootItemCondition {
    public static final MapCodec<LootConditionSpreading> CODEC = MapCodec.unit(LootConditionSpreading::new);
    public static LootItemConditionType TYPE;

    public static void reg() {
        TYPE = Registry.register(
                BuiltInRegistries.LOOT_CONDITION_TYPE,
                ResourceLocation.fromNamespaceAndPath(BCFactory.MODID, "spreading"),
                new LootItemConditionType(CODEC)
        );
    }

    public LootConditionSpreading() {
    }

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        return context.getParam(LootContextParams.BLOCK_STATE).getValue(BlockWaterGel.PROP_STAGE).spreading;
    }

    public static LootItemCondition.Builder builder() {
        return () -> new LootConditionSpreading();
    }

}
