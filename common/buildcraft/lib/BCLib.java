/* Copyright (c) 2016 SpaceToad and the BuildCraft team
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package buildcraft.lib;

import buildcraft.api.BCBlocks;
import buildcraft.api.BCItems;

import buildcraft.api.BCModules;
import buildcraft.api.core.BCLog;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.recipes.IRefineryRecipeManager;
import buildcraft.api.tiles.TilesAPI;
import buildcraft.api.transport.pipe.PipeApi;
import buildcraft.lib.block.VanillaPaintHandlers;
import buildcraft.lib.block.VanillaRotationHandlers;
import buildcraft.lib.chunkload.ChunkLoaderManager;
import buildcraft.lib.expression.ExpressionDebugManager;
import buildcraft.lib.list.VanillaListHandlers;
import buildcraft.lib.marker.MarkerCache;
import buildcraft.lib.misc.CapUtil;
import buildcraft.lib.misc.ExpressionCompat;
import buildcraft.lib.net.MessageManager;
import buildcraft.lib.net.cache.BuildCraftObjectCaches;
import buildcraft.lib.registry.CreativeTabManager;
import buildcraft.lib.registry.MigrationManager;
import buildcraft.lib.registry.TagManager;
import buildcraft.lib.registry.TagManager.TagEntry;
import buildcraft.lib.script.ReloadableRegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

//@formatter:off
//@Mod(
//        modid = BCLib.MODID,
//        name = "BuildCraft Lib",
//        version = BCLib.VERSION,
//        updateJSON = "https://mod-buildcraft.com/version/versions.json",
//        acceptedMinecraftVersions = "(gradle_replace_mcversion,)",
//        dependencies = "required-after:forge@(gradle_replace_forgeversion,)"
//)
@Mod(BCLib.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
//@formatter:on
public class BCLib {
    public static final String MODID = "buildcraftlib";
    /** Runtime mod version from the jar manifest; the placeholder is retained for dev class directories. */
    public static final String VERSION = getImplementationVersion();
    public static final String MC_VERSION = "1.21.1";
    public static final String GIT_BRANCH = "${git_branch}";
    public static final String GIT_COMMIT_HASH = "${git_commit_hash}";
    public static final String GIT_COMMIT_MSG = "${git_commit_msg}";
    public static final String GIT_COMMIT_AUTHOR = "${git_commit_author}";

    public static final boolean DEV = VERSION.startsWith("$") || Boolean.getBoolean("buildcraft.dev");

    private static String getImplementationVersion() {
        String version = BCLib.class.getPackage().getImplementationVersion();
        return version == null || version.isBlank() ? "$version" : version;
    }

    // @Instance(MODID)
    public static BCLib INSTANCE;

    public static ModContainer MOD_CONTAINER;

    static {
        // Calen: should be called before BCSiliconPlugs.preInit() and BCTransportPlugs.preInit()
        BCLibRegistries.fmlPreInit();
    }

    public BCLib() {
        INSTANCE = this;

        ExpressionCompat.setup();
    }

    @SubscribeEvent
    public static void onRegisterEvent(RegisterEvent event) {
        ResourceKey<? extends Registry<?>> registry = event.getRegistryKey();
        event.register(Registries.RECIPE_TYPE, IRefineryRecipeManager.IHeatableRecipe.TYPE_ID, () -> IRefineryRecipeManager.IHeatableRecipe.TYPE);
        event.register(Registries.RECIPE_TYPE, IRefineryRecipeManager.ICoolableRecipe.TYPE_ID, () -> IRefineryRecipeManager.ICoolableRecipe.TYPE);
        event.register(Registries.RECIPE_TYPE, IRefineryRecipeManager.IDistillationRecipe.TYPE_ID, () -> IRefineryRecipeManager.IDistillationRecipe.TYPE);
        BCLibMenuTypes.registerAll(event);
        if (registry == Registries.BLOCK) {


            BCLibRegistries.initRecipeRegistry();

            // GUI
        }
    }

    @SubscribeEvent
    public static void preInit(FMLConstructModEvent evt) {
        IEventBus modEventBus = ((FMLModContainer) ModList.get().getModContainerById(MODID).orElseThrow()).getEventBus();
        // Cap
        modEventBus.addListener(CapUtil::registerCapability);
        modEventBus.addListener(MjAPI::registerCapability);
        modEventBus.addListener(TilesAPI::registerCapability);
        modEventBus.addListener(PipeApi::registerCapability);
        modEventBus.addListener(MessageManager::registerPayloads);
        // CreativeModTab
        modEventBus.addListener(CreativeTabManager::addItemsToVanillaTabs);

        modEventBus.register(BCLibEventDistModBus.INSTANCE);

        MOD_CONTAINER = ModList.get().getModContainerById(MODID).get();

        try {
            BCLog.logger.info("");
        } catch (NoSuchFieldError e) {
            throw throwBadClass(e, BCLog.class);
        }
        BCLog.logger.info("Starting BuildCraft " + BCLib.VERSION);
        BCLog.logger.info("Copyright (c) the BuildCraft team, 2011-2018");
        BCLog.logger.info("https://www.mod-buildcraft.com");
        if (!GIT_COMMIT_HASH.startsWith("${")) {
            BCLog.logger.info("Detailed Build Information:");
            BCLog.logger.info("  Branch " + GIT_BRANCH);
            BCLog.logger.info("  Commit " + GIT_COMMIT_HASH);
            BCLog.logger.info("    " + GIT_COMMIT_MSG);
            BCLog.logger.info("    committed by " + GIT_COMMIT_AUTHOR);
        }
        BCLog.logger.info("");
        BCLog.logger.info("Loaded Modules:");
        for (BCModules module : BCModules.VALUES) {
            if (module.isLoaded()) {
                BCLog.logger.info("  - " + module.lowerCaseName);
            }
        }
        BCLog.logger.info("Missing Modules:");
        for (BCModules module : BCModules.VALUES) {
            if (!module.isLoaded()) {
                BCLog.logger.info("  - " + module.lowerCaseName);
            }
        }
        BCLog.logger.info("");

        ExpressionDebugManager.logger = BCLog.logger::info;
//        ExpressionCompat.setup(); // Calen: moved to <init> to be loaded early enough, or the Silicon/Transport/Factory model classed will cause Exception when running <clinit>


//        BCLibRegistries.fmlPreInit(); // Calen: moved to static
        BCLibProxy.getProxy().fmlPreInit();
        BCLibItems.fmlPreInit();
//
        BuildCraftObjectCaches.fmlPreInit();
//        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, BCLibProxy.getProxy());
//
        NeoForge.EVENT_BUS.register(BCLibEventDist.INSTANCE);
//        NeoForge.EVENT_BUS.register(FluidManager.class); // Calen: not used in 1.18.2

//        // Set max chunk limit for quarries: 1 chunk for quarry itself and 5 * 5 chunks square for working area
//        ForgeChunkManager.getConfig().get(MODID, "maximumChunksPerTicket", 26);
//        ForgeChunkManager.syncConfigDefaults();
//        ForgeChunkManager.setForcedChunkLoadingCallback(BCLib.INSTANCE, ChunkLoaderManager::rebindTickets);
        ChunkLoaderManager.registerTicketController(modEventBus);
    }

    public static Error throwBadClass(Error e, Class<?> cls) throws Error {
        throw new Error(
                "Bad " + cls + " loaded from " + cls.getClassLoader() + " domain: " + cls.getProtectionDomain(), e
        );
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent evt) {
        BCBlocks.init();
        BCItems.init();
        BCLibProxy.getProxy().fmlInit();

        BCLibRegistries.fmlInit(); // did nothing in 1.12.2 as well
        VanillaListHandlers.fmlInit();
        VanillaPaintHandlers.fmlInit();
        VanillaRotationHandlers.fmlInit();

//        RegistrationHelper.registerOredictEntries(); // 1.18.2 oredict -> datagen
    }

    @SubscribeEvent
    public static void postInit(FMLLoadCompleteEvent evt) {
        ReloadableRegistryManager.loadAll();
        BCLibProxy.getProxy().fmlPostInit();
        BuildCraftObjectCaches.fmlPostInit();
        VanillaListHandlers.fmlPostInit();
        MarkerCache.postInit();
        MessageManager.fmlPostInit();
    }

    // Calen: moved to BCLibEventDistForgeBus#serverStarting because it is Forge Bus Event in 1.18.2
//    @Mod.EventHandler
//    public static void serverStarting(FMLServerStartingEvent event) {
//        event.registerServerCommand(new CommandBuildCraft());
//    }

    private static final TagManager tagManager = new TagManager();

    static {
        startBatch();
        registerTag("item.guide").reg("guide").locale("buildcraft.guide")
//                .model("guide")
//                .tab("vanilla.misc")
                .tab("buildcraft.main")
        ;
        registerTag("item.guide.note").reg("guide_note").locale("buildcraft.guide_note")
//                .model("guide_note")
//                .tab("vanilla.misc")
                .tab("buildcraft.main")
        ;
        registerTag("item.debugger").reg("debugger").locale("debugger")
//                .model("debugger")
//                .tab("vanilla.misc")
                .tab("buildcraft.main")
        ;
//        endBatch(TagManager.prependTags("buildcraftlib:", TagManager.EnumTagType.REGISTRY_NAME, TagManager.EnumTagType.MODEL_LOCATION));
        endBatch(TagManager.prependTags("buildcraftlib:", TagManager.EnumTagType.REGISTRY_NAME));
    }

    private static TagEntry registerTag(String id) {
//        return TagManager.registerTag(id);
        return tagManager.registerTag(id);
    }

    private static void startBatch() {
//        TagManager.startBatch();
        tagManager.startBatch();
    }

    private static void endBatch(Consumer<TagEntry> consumer) {
//        TagManager.endBatch(consumer);
        tagManager.endBatch(consumer);
    }
}
