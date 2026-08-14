package buildcraft.datagen.core;

import buildcraft.core.BCCore;
import buildcraft.core.BCCoreBlocks;
import buildcraft.core.BCCoreItems;
import buildcraft.datagen.base.BCBaseAdvancementGenerator;
import buildcraft.lib.BCLibItems;
import buildcraft.lib.oredictionarytag.OreDictionaryTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CoreAdvancementGenerator extends BCBaseAdvancementGenerator {
    private static final String NAMESPACE = BCCore.MODID;

    public CoreAdvancementGenerator(PackOutput output, ExistingFileHelper fileHelperIn, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, fileHelperIn, registries);
    }

    @Override
    protected void registerAdvancements(Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper, HolderLookup.Provider registries) {
        // root
        AdvancementHolder root = Advancement.Builder.advancement().display(
                        BCCoreItems.gearWood.get(),
                        Component.translatable("advancements.buildcraftcore.root.title"),
                        Component.translatable("advancements.buildcraftcore.root.description"),
                        ResourceLocation.parse("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementType.TASK,
                        false, false, false)
                .requirements(Strategy.OR)
                .addCriterion("has_stick",
                        InventoryChangeTrigger.TriggerInstance.hasItems(tag(Tags.Items.RODS_WOODEN))
                )
                .save(consumer, NAMESPACE + ":root");
        ROOT = root;
        // gears
        AdvancementHolder gears = Advancement.Builder.advancement().display(
                        BCCoreItems.gearDiamond.get(),
                        Component.translatable("advancements.buildcraftcore.gears.title"),
                        Component.translatable("advancements.buildcraftcore.gears.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false)
                .parent(root)
                .requirements(Strategy.AND)
                .addCriterion("gear_wood",
                        InventoryChangeTrigger.TriggerInstance.hasItems(tag(OreDictionaryTags.GEAR_WOOD))
                )
                .addCriterion("gear_stone",
                        InventoryChangeTrigger.TriggerInstance.hasItems(tag(OreDictionaryTags.GEAR_STONE))
                )
                .addCriterion("gear_iron",
                        InventoryChangeTrigger.TriggerInstance.hasItems(tag(OreDictionaryTags.GEAR_IRON))
                )
                .addCriterion("gear_gold",
                        InventoryChangeTrigger.TriggerInstance.hasItems(tag(OreDictionaryTags.GEAR_GOLD))
                )
                .addCriterion("gear_diamond",
                        InventoryChangeTrigger.TriggerInstance.hasItems(tag(OreDictionaryTags.GEAR_DIAMOND))
                )
                .save(consumer, NAMESPACE + ":gears");
        GEARS = gears;
        // wrenched
        AdvancementHolder wrenched = Advancement.Builder.advancement().display(
                        BCCoreItems.wrench.get(),
                        Component.translatable("advancements.buildcraftcore.wrenched.title"),
                        Component.translatable("advancements.buildcraftcore.wrenched.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(root)
                .requirements(Strategy.OR)
                .addCriterion(
                        "code_trigger",
                        IMPOSSIBLE
                )
                .save(consumer, NAMESPACE + ":wrenched");
        WRENCHED = wrenched;
        // free_power
        AdvancementHolder free_power = Advancement.Builder.advancement().display(
                        BCCoreBlocks.engineWood.get(),
                        Component.translatable("advancements.buildcraftcore.freePowar.title"),
                        Component.translatable("advancements.buildcraftcore.freePowar.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(wrenched)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":free_power");
        // guide
        AdvancementHolder guide = Advancement.Builder.advancement().display(
                        BCLibItems.guide.get(),
                        Component.translatable("advancements.buildcraftcore.guide.title"),
                        Component.translatable("advancements.buildcraftcore.guide.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(root)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":guide");
        GUIDE = guide;
        // markers
        AdvancementHolder markers = Advancement.Builder.advancement().display(
                        BCCoreBlocks.markerVolume.get(),
                        Component.translatable("advancements.buildcraftcore.markers.title"),
                        Component.translatable("advancements.buildcraftcore.markers.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(guide)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":markers");
        MARKERS = markers;
        // list
        AdvancementHolder list = Advancement.Builder.advancement().display(
                        BCCoreItems.list.get(),
                        Component.translatable("advancements.buildcraftcore.list.title"),
                        Component.translatable("advancements.buildcraftcore.list.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(guide)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":list");
        // paper
        AdvancementHolder paper = Advancement.Builder.advancement().display(
                        Items.PAPER,
                        Component.translatable("advancements.buildcraftcore.paper.title"),
                        Component.translatable("advancements.buildcraftcore.paper.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(list)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":paper");
        // goggles
        AdvancementHolder goggles = Advancement.Builder.advancement().display(
                        // TODO Calen goggles texture
//                        BCCoreItems.GOOGLES.get(),
                        Items.IRON_HELMET,
                        Component.translatable("advancements.buildcraftcore.goggles.title"),
                        Component.translatable("advancements.buildcraftcore.goggles.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(guide)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":goggles");
        // path_markers
        AdvancementHolder path_markers = Advancement.Builder.advancement().display(
                        BCCoreBlocks.markerPath.get(),
                        Component.translatable("advancements.buildcraftcore.path_markers.title"),
                        Component.translatable("advancements.buildcraftcore.path_markers.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(markers)
                .requirements(Strategy.OR)
                .addCriterion("code_trigger", IMPOSSIBLE)
                .save(consumer, NAMESPACE + ":path_markers");
    }

    @Override
    public String getName() {
        return "BuildCraft Core Advancement Generator";
    }
}
