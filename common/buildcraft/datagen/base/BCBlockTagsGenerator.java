package buildcraft.datagen.base;

import buildcraft.api.BCModules;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.core.BCCoreBlocks;
import buildcraft.energy.BCEnergyBlocks;
import buildcraft.factory.BCFactoryBlocks;
import buildcraft.lib.oredictionarytag.OreDictionaryTags;
import buildcraft.robotics.BCRoboticsBlocks;
import buildcraft.silicon.BCSiliconBlocks;
import buildcraft.transport.BCTransportBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BCBlockTagsGenerator extends BlockTagsProvider {
    public BCBlockTagsGenerator(PackOutput packOutput, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, BCModules.BUILDCRAFT, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        // bedrock_like
        tag(BlockTags.DRAGON_IMMUNE)
                .addOptional(BCCoreBlocks.springWater.getId())
                .addOptional(BCCoreBlocks.springOil.getId())
                .addOptional(BCFactoryBlocks.tube.getId())
        ;

        tag(BlockTags.WITHER_IMMUNE)
                .addOptional(BCCoreBlocks.springWater.getId())
                .addOptional(BCCoreBlocks.springOil.getId())
                .addOptional(BCFactoryBlocks.tube.getId())
        ;

        // pickaxe mineable
        TagAppender<Block> mineable_with_pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addOptional(BCEnergyBlocks.mjDynamo.getId())
                .addOptional(BCFactoryBlocks.autoWorkbenchItems.getId())
                .addOptional(BCFactoryBlocks.chute.getId())
                .addOptional(BCFactoryBlocks.distiller.getId())
                .addOptional(BCFactoryBlocks.heatExchange.getId())
                .addOptional(BCFactoryBlocks.tank.getId())
                .addOptional(BCFactoryBlocks.pump.getId())
                .addOptional(BCFactoryBlocks.miningWell.getId())
                .addOptional(BCFactoryBlocks.waterGel.getId())
                .addOptional(BCFactoryBlocks.floodGate.getId())
                .addOptional(BCBuildersBlocks.quarry.getId())
                .addOptional(BCBuildersBlocks.builder.getId())
                .addOptional(BCBuildersBlocks.architect.getId())
                .addOptional(BCBuildersBlocks.filler.getId())
                .addOptional(BCBuildersBlocks.library.getId())
                .addOptional(BCBuildersBlocks.replacer.getId())
                .addOptional(BCSiliconBlocks.integrationTable.getId())
                .addOptional(BCSiliconBlocks.assemblyTable.getId())
                .addOptional(BCSiliconBlocks.advancedCraftingTable.getId())
                .addOptional(BCSiliconBlocks.programmingTable.getId())
                .addOptional(BCSiliconBlocks.chargingTable.getId())
                .addOptional(BCSiliconBlocks.laser.getId())
                .addOptional(BCRoboticsBlocks.zonePlanner.getId())
                .addOptional(BCRoboticsBlocks.requester.getId())
                .addOptional(BCTransportBlocks.filteredBuffer.getId());
        BCCoreBlocks.engineBlockMap.values().stream().forEach(reg -> mineable_with_pickaxe.addOptional(reg.getId()));

        tag(OreDictionaryTags.WORKBENCHES_BLOCK)
                .addOptional(Blocks.CRAFTING_TABLE.builtInRegistryHolder().key().location())
        ;

        tag(OreDictionaryTags.SOFT)
                .addOptional(Blocks.AIR.builtInRegistryHolder().key().location())
                .addOptional(Blocks.SNOW.builtInRegistryHolder().key().location())
                .addOptional(Blocks.VINE.builtInRegistryHolder().key().location())
                .addOptional(Blocks.FIRE.builtInRegistryHolder().key().location())
        ;
    }

    @Override
    public String getName() {
        return "BuildCraft Block Tags Generator";
    }
}
