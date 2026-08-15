package buildcraft.datagen.silicon;

import buildcraft.datagen.base.BCBaseAdvancementGenerator;
import buildcraft.silicon.BCSilicon;
import buildcraft.silicon.BCSiliconBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SiliconAdvancementGenerator extends BCBaseAdvancementGenerator {
    private static final String NAMESPACE = BCSilicon.MODID;

    public SiliconAdvancementGenerator(PackOutput output, ExistingFileHelper fileHelperIn, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, fileHelperIn, registries);
    }

    @Override
    protected void registerAdvancements(Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper, HolderLookup.Provider registries) {
        // fluid_storage
        AdvancementHolder laser_power = Advancement.Builder.advancement().display(
                        BCSiliconBlocks.laser.get(),
                        Component.translatable("advancements.buildcraftsilicon.laser_power.title"),
                        Component.translatable("advancements.buildcraftsilicon.laser_power.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(ROOT)
                .requirements(Strategy.OR)
                .addCriterion("get_laser",
                        InventoryChangeTrigger.TriggerInstance.hasItems(BCSiliconBlocks.laser.get())
                )
                .save(consumer, NAMESPACE + ":laser_power");
        // precision_crafting
        AdvancementHolder precision_crafting = Advancement.Builder.advancement().display(
                        BCSiliconBlocks.assemblyTable.get(),
                        Component.translatable("advancements.buildcraftsilicon.precision_crafting.title"),
                        Component.translatable("advancements.buildcraftsilicon.precision_crafting.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(laser_power)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":precision_crafting");
    }

    @Override
    public String getName() {
        return "BuildCraft Silicon Advancement Generator";
    }
}
