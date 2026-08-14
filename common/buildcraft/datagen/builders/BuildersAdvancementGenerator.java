package buildcraft.datagen.builders;

import buildcraft.builders.BCBuilders;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.core.BCCoreBlocks;
import buildcraft.datagen.base.BCBaseAdvancementGenerator;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BuildersAdvancementGenerator extends BCBaseAdvancementGenerator {
    private static final String NAMESPACE = BCBuilders.MODID;

    public BuildersAdvancementGenerator(PackOutput output, ExistingFileHelper fileHelperIn, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, fileHelperIn, registries);
    }

    @Override
    protected void registerAdvancements(Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper, HolderLookup.Provider registries) {
        // architect
        AdvancementHolder architect = Advancement.Builder.advancement().display(
                        BCBuildersBlocks.architect.get(),
                        Component.translatable("advancements.buildcraftbuilders.architect.title"),
                        Component.translatable("advancements.buildcraftbuilders.architect.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .parent(MARKERS)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":architect");
        // shaping_the_world
        AdvancementHolder shaping_the_world = Advancement.Builder.advancement().display(
                        BCBuildersBlocks.quarry.get(),
                        Component.translatable("advancements.buildcraftbuilders.shaping_the_world.title"),
                        Component.translatable("advancements.buildcraftbuilders.shaping_the_world.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .parent(GEARS)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":shaping_the_world");
        // building_for_the_future
        AdvancementHolder building_for_the_future = Advancement.Builder.advancement().display(
                        BCBuildersBlocks.filler.get(),
                        Component.translatable("advancements.buildcraftbuilders.building_for_the_future.title"),
                        Component.translatable("advancements.buildcraftbuilders.building_for_the_future.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false)
                .parent(shaping_the_world)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":building_for_the_future");
        // diggy_diggy_hole
        AdvancementHolder diggy_diggy_hole = Advancement.Builder.advancement().display(
                        BCBuildersBlocks.quarry.get(),
                        Component.translatable("advancements.buildcraftbuilders.diggy.title"),
                        Component.translatable("advancements.buildcraftbuilders.diggy.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false)
                .parent(shaping_the_world)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":diggy_diggy_hole");
        // destroying_the_world
        AdvancementHolder destroying_the_world = Advancement.Builder.advancement().display(
                        BCBuildersBlocks.filler.get(),
                        Component.translatable("advancements.buildcraftbuilders.destroying_the_world.title"),
                        Component.translatable("advancements.buildcraftbuilders.destroying_the_world.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .parent(diggy_diggy_hole)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":destroying_the_world");
        // paving_the_way
        AdvancementHolder paving_the_way = Advancement.Builder.advancement().display(
                        BCCoreBlocks.markerPath.get(),
                        Component.translatable("advancements.buildcraftbuilders.paving_the_way.title"),
                        Component.translatable("advancements.buildcraftbuilders.paving_the_way.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .parent(architect)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":paving_the_way");
        // start_of_something_big
        AdvancementHolder start_of_something_big = Advancement.Builder.advancement().display(
                        BCBuildersBlocks.builder.get(),
                        Component.translatable("advancements.buildcraftbuilders.start_of_something_big.title"),
                        Component.translatable("advancements.buildcraftbuilders.start_of_something_big.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false)
                .parent(architect)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":start_of_something_big");
    }

    @Override
    public String getName() {
        return "BuildCraft Builders Advancement Generator";
    }
}
