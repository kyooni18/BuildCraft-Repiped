package buildcraft.datagen.energy;

import buildcraft.core.BCCoreBlocks;
import buildcraft.core.BCCoreItems;
import buildcraft.datagen.base.BCBaseAdvancementGenerator;
import buildcraft.energy.BCEnergy;
import buildcraft.energy.BCEnergyBlocks;
import buildcraft.energy.BCEnergyFluids;
import buildcraft.energy.BCEnergyItems;
import buildcraft.energy.generation.biome.BCBiomeRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EnergyAdvancementGenerator extends BCBaseAdvancementGenerator {
    private static final String NAMESPACE = BCEnergy.MODID;

    public EnergyAdvancementGenerator(PackOutput output, ExistingFileHelper fileHelperIn, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, fileHelperIn, registries);
    }

    @Override
    protected void registerAdvancements(Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper, HolderLookup.Provider registries) {
        // engine
        AdvancementHolder engine = Advancement.Builder.advancement().display(
                        BCEnergyBlocks.engineStone.get(),
                        Component.translatable("advancements.buildcraftcore.engine.title"),
                        Component.translatable("advancements.buildcraftcore.engine.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(GUIDE)
                .requirements(Strategy.OR)
                .addCriterion(
                        "redstone_engine",
                        InventoryChangeTrigger.TriggerInstance.hasItems(BCCoreBlocks.engineWood.get())
                )
                .addCriterion(
                        "strirling_engine",
                        InventoryChangeTrigger.TriggerInstance.hasItems(BCEnergyBlocks.engineStone.get())
                )
                .addCriterion(
                        "combustion_engine",
                        InventoryChangeTrigger.TriggerInstance.hasItems(BCEnergyBlocks.engineIron.get())
                )
                .save(consumer, NAMESPACE + ":engine");
        // powering_up
        AdvancementHolder powering_up = Advancement.Builder.advancement().display(
                        BCEnergyBlocks.engineStone.get(),
                        Component.translatable("advancements.buildcraftenergy.poweringUp.title"),
                        Component.translatable("advancements.buildcraftenergy.poweringUp.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(engine)
                .requirements(Strategy.OR)
                .addCriterion(
                        "code_trigger",
                        IMPOSSIBLE
                )
                .save(consumer, NAMESPACE + ":powering_up");
        // lava_power
        AdvancementHolder lava_power = Advancement.Builder.advancement().display(
                        Items.LAVA_BUCKET,
                        Component.translatable("advancements.buildcraftenergy.lava_power.title"),
                        Component.translatable("advancements.buildcraftenergy.lava_power.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(engine)
                .requirements(Strategy.OR)
                .addCriterion(
                        "code_trigger",
                        IMPOSSIBLE
                )
                .save(consumer, NAMESPACE + ":lava_power");
        // ice_cool
        AdvancementHolder ice_cool = Advancement.Builder.advancement().display(
                        Items.WATER_BUCKET,
                        Component.translatable("advancements.buildcraftenergy.ice_cool.title"),
                        Component.translatable("advancements.buildcraftenergy.ice_cool.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(powering_up)
                .requirements(Strategy.OR)
                .addCriterion(
                        "code_trigger",
                        IMPOSSIBLE
                )
                .save(consumer, NAMESPACE + ":ice_cool");
        // fine_riches
        AdvancementHolder fine_riches = Advancement.Builder.advancement().display(
                        BCEnergyItems.globOil.get(),
                        Component.translatable("advancements.buildcraftenergy.fine_riches.title"),
                        Component.translatable("advancements.buildcraftenergy.fine_riches.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .parent(ROOT)
                .requirements(Strategy.OR)
                .addCriterion(
                        "oil_desert_biome",
                        PlayerTrigger.TriggerInstance.located(
                                LocationPredicate.Builder.inBiome(registries.lookupOrThrow(Registries.BIOME).getOrThrow(BCBiomeRegistry.RESOURCE_KEY_BIOME_OIL_DESERT))
                        )
                )
                .addCriterion(
                        "oil_ocean_biome",
                        PlayerTrigger.TriggerInstance.located(
                                LocationPredicate.Builder.inBiome(registries.lookupOrThrow(Registries.BIOME).getOrThrow(BCBiomeRegistry.RESOURCE_KEY_BIOME_OIL_OCEAN))
                        )
                )
                .save(consumer, NAMESPACE + ":fine_riches");
        // sticky_dipping
        AdvancementHolder sticky_dipping = Advancement.Builder.advancement().display(
                        BCEnergyItems.globOil.get(),
                        Component.translatable("advancements.buildcraftenergy.sticky_dipping.title"),
                        Component.translatable("advancements.buildcraftenergy.sticky_dipping.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, true
                )
                .parent(fine_riches)
                .requirements(Strategy.OR)
                .addCriterion(
                        "oil",
                        EnterBlockTrigger.TriggerInstance.entersBlock(BCEnergyFluids.crudeOil[0].get().getReg().getBlock())
                )
                .save(consumer, NAMESPACE + ":sticky_dipping");
        // refine_and_redefine
        AdvancementHolder refine_and_redefine = Advancement.Builder.advancement().display(
                        BCEnergyItems.globOil.get(),
                        Component.translatable("advancements.buildcraftenergy.refine_and_redefine.title"),
                        Component.translatable("advancements.buildcraftenergy.refine_and_redefine.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .parent(sticky_dipping)
                .requirements(Strategy.OR)
                .addCriterion(
                        "code_trigger",
                        IMPOSSIBLE
                )
                .save(consumer, NAMESPACE + ":refine_and_redefine");
        // to_much_power
        AdvancementHolder to_much_power = Advancement.Builder.advancement().display(
                        BCCoreItems.wrench.get(),
                        Component.translatable("advancements.buildcraftenergy.to_much_power.title"),
                        Component.translatable("advancements.buildcraftenergy.to_much_power.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(powering_up)
                .requirements(Strategy.OR)
                .addCriterion(
                        "code_trigger",
                        IMPOSSIBLE
                )
                .save(consumer, NAMESPACE + ":to_much_power");
    }

    @Override
    public String getName() {
        return "BuildCraft Energy Advancement Generator";
    }
}
