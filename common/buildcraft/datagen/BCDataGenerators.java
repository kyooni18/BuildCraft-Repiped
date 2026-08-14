package buildcraft.datagen;

import buildcraft.api.BCModules;
import buildcraft.api.core.BCLog;
import buildcraft.core.BCCore;
import buildcraft.datagen.base.*;
import buildcraft.datagen.builders.*;
import buildcraft.datagen.core.*;
import buildcraft.datagen.energy.*;
import buildcraft.datagen.factory.*;
import buildcraft.datagen.lib.LibCraftingRecipeGenerator;
import buildcraft.datagen.lib.LibItemModelProvider;
import buildcraft.datagen.robotics.*;
import buildcraft.datagen.silicon.*;
import buildcraft.datagen.transport.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BCCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BCDataGenerators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
        DataGenerator.PackGenerator packGenerator = generator.getVanillaPack(true);

        // Worldgen
        packGenerator.addProvider(packOutput -> new BCDatapackBuiltinEntriesProvider(packOutput, lookupProvider));

        // Oil Texture
        packGenerator.addProvider(packOutput -> new EnergyOilTextureGenerator(packOutput, existingFileHelper));

//        // frozen fluid texture
//        generator.addProvider(new FrozenFluidTextureProvider(generator, existingFileHelper));

        // Tags
        BCBlockTagsGenerator blockTagsProvider = packGenerator.addProvider(packOutput -> new BCBlockTagsGenerator(packOutput, lookupProvider, existingFileHelper));
        packGenerator.addProvider(packOutput -> new BCItemTagsGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        packGenerator.addProvider(packOutput -> new BCFluidTagsGenerator(packOutput, lookupProvider, existingFileHelper));
        packGenerator.addProvider(packOutput -> new BCBiomeTagsGenerator(packOutput, lookupProvider, existingFileHelper));

        // Crafting Recipes
        packGenerator.addProvider(packOutput -> new BuildersCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new CoreCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new EnergyCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new FactoryCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new LibCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new SiliconCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new TransportCraftingRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new RoboticsCraftingRecipeGenerator(packOutput, lookupProvider));
        // Mod Recipes
        packGenerator.addProvider(packOutput -> new SiliconFacadeSwapRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new EnergyOilRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new SiliconAssemblyRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new TransportAssemblyRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new RoboticsIntegrationRecipeGenerator(packOutput, lookupProvider));
        packGenerator.addProvider(packOutput -> new RoboticsProgrammingRecipeGenerator(packOutput, lookupProvider));

        // Advancement
        packGenerator.addProvider(packOutput -> new CoreAdvancementGenerator(packOutput, existingFileHelper, lookupProvider));
        packGenerator.addProvider(packOutput -> new EnergyAdvancementGenerator(packOutput, existingFileHelper, lookupProvider));
        packGenerator.addProvider(packOutput -> new FactoryAdvancementGenerator(packOutput, existingFileHelper, lookupProvider));
        packGenerator.addProvider(packOutput -> new SiliconAdvancementGenerator(packOutput, existingFileHelper, lookupProvider));
        packGenerator.addProvider(packOutput -> new TransportAdvancementGenerator(packOutput, existingFileHelper, lookupProvider));
        packGenerator.addProvider(packOutput -> new BuildersAdvancementGenerator(packOutput, existingFileHelper, lookupProvider));

        // Loot Table
        packGenerator.addProvider(packOutput -> new BCBaseLootTableProvider(BCModules.CORE, packOutput, Set.of(), ImmutableList.of(new LootTableProvider.SubProviderEntry(CoreBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        packGenerator.addProvider(packOutput -> new BCBaseLootTableProvider(BCModules.ENERGY, packOutput, Set.of(), ImmutableList.of(new LootTableProvider.SubProviderEntry(EnergyBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        packGenerator.addProvider(packOutput -> new BCBaseLootTableProvider(BCModules.FACTORY, packOutput, Set.of(), ImmutableList.of(new LootTableProvider.SubProviderEntry(FactoryBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        packGenerator.addProvider(packOutput -> new BCBaseLootTableProvider(BCModules.SILICON, packOutput, Set.of(), ImmutableList.of(new LootTableProvider.SubProviderEntry(SiliconBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        packGenerator.addProvider(packOutput -> new BCBaseLootTableProvider(BCModules.TRANSPORT, packOutput, Set.of(), ImmutableList.of(new LootTableProvider.SubProviderEntry(TransportBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        packGenerator.addProvider(packOutput -> new BCBaseLootTableProvider(BCModules.BUILDERS, packOutput, Set.of(), ImmutableList.of(new LootTableProvider.SubProviderEntry(BuildersBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));

        // BlockState and Block Model
        packGenerator.addProvider(packOutput -> new CoreBlockStateGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new BuildersBlockStateGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new EnergyBlockStateGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new FactoryBlockStateGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new SiliconBlockStateGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new TransportBlockStateGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new RoboticsBlockStateGenerator(packOutput, existingFileHelper));

        // Item Model
        packGenerator.addProvider(packOutput -> new EnergyOilBucketModelGenerator(packOutput, existingFileHelper));

        packGenerator.addProvider(packOutput -> new CoreItemModelGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new EnergyItemModelGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new FactoryItemModelGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new BuildersItemModelGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new SiliconItemModelGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new TransportItemModelGenerator(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new LibItemModelProvider(packOutput, existingFileHelper));
        packGenerator.addProvider(packOutput -> new RoboticsItemModelGenerator(packOutput, existingFileHelper));

        // Sprite
        // Calen 1.20.1: 1 mod jar should contain at most 1 SpriteSourceProvider for BLOCKS_ATLAS, more will overwrite the earlier /assets/minecraft/atlases/blocks.json files
        packGenerator.addProvider(packOutput -> new BCSpriteSourceProvider(packOutput, existingFileHelper));

        // Calen 1.20.1
        enableShouldExecute(generator);
    }

    // Calen 1.20.1 for datagen
    private static void enableShouldExecute(DataGenerator generator) {
        try {
            Field f_dataGeneratorConfig = DatagenModLoader.class.getDeclaredField("dataGeneratorConfig");
            f_dataGeneratorConfig.setAccessible(true);
            GatherDataEvent.DataGeneratorConfig config = (GatherDataEvent.DataGeneratorConfig) f_dataGeneratorConfig.get(null);
            Field f_generators = GatherDataEvent.DataGeneratorConfig.class.getDeclaredField("generators");
            f_generators.setAccessible(true);
            List<DataGenerator> generators = (List<DataGenerator>) f_generators.get(config);
            if (!generators.contains(generator)) {
                generators.add(generator);
            }
        } catch (Exception e) {
            BCLog.logger.error(e);
        }
    }
}
