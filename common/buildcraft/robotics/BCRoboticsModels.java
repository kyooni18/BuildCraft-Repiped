package buildcraft.robotics;

import buildcraft.api.BCModules;
import buildcraft.api.transport.pipe.PipeApiClient;
import buildcraft.api.transport.pluggable.IPluggableStaticBaker;
import buildcraft.lib.client.model.ModelHolderStatic;
import buildcraft.lib.client.model.ModelHolderVariable;
import buildcraft.lib.client.model.ModelPluggableItem;
import buildcraft.lib.client.model.plug.PlugBakerSimple;
import buildcraft.lib.expression.FunctionContext;
import buildcraft.lib.misc.ExpressionCompat;
import buildcraft.lib.misc.RegistryUtil;
import buildcraft.robotics.client.model.RoboticsNodeTypes;
import buildcraft.robotics.client.model.key.KeyPlugRobotStation;
import buildcraft.robotics.client.render.PlugRobotStationRenderer;
import buildcraft.robotics.client.render.RenderRobot;
import buildcraft.robotics.client.render.RenderZonePlanner;
import buildcraft.robotics.plug.PluggableRobotStation;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.LazyLoadedValue;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BCRoboticsModels {
    public static final ModelHolderStatic ROBOT_STATION_STATIC;
    public static final ModelHolderVariable ROBOT_STATION_DYNAMIC;

    public static final IPluggableStaticBaker<KeyPlugRobotStation> BAKER_PLUG_ROBOT_STATION;

    static {
        // Calen: ensure ExpressionCompat ENUM_FACING = new NodeType<>("Facing", Direction.UP); run, or will cause IllegalArgumentException: Unknown NodeType class net.minecraft.core.Direction
        ExpressionCompat.setup();
        RoboticsNodeTypes.setup();

        ROBOT_STATION_STATIC = getStaticModel("plugs/robot_station_static");
        ROBOT_STATION_DYNAMIC = getModel("plugs/robot_station_dynamic", PluggableRobotStation.MODEL_FUNC_CTX);

        BAKER_PLUG_ROBOT_STATION = new PlugBakerSimple<>(ROBOT_STATION_STATIC::getCutoutQuads);
    }

    private static ModelHolderStatic getStaticModel(String str) {
        return new ModelHolderStatic("buildcraftrobotics:models/" + str + ".json");
    }

    private static ModelHolderVariable getModel(String str, FunctionContext fnCtx) {
        return new ModelHolderVariable("buildcraftrobotics:models/" + str + ".json", fnCtx);
    }

    public static void fmlPreInit() {
        IEventBus modEventBus = ((FMLModContainer) ModList.get().getModContainerById(BCRobotics.MODID).get()).getEventBus();
        modEventBus.register(BCRoboticsModels.class);
    }

    public static void fmlInit() {
        PipeApiClient.IClientRegistry pipeRegistryClient = PipeApiClient.registry;
        if (pipeRegistryClient != null) {
            pipeRegistryClient.registerBaker(KeyPlugRobotStation.class, BAKER_PLUG_ROBOT_STATION);
            pipeRegistryClient.registerRenderer(PluggableRobotStation.class, PlugRobotStationRenderer.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onRendererReg(EntityRenderersEvent.RegisterRenderers event) {
        RegistryUtil.regTesrIfTilePresent(BCRoboticsBlocks.zonePlannerTile, RenderZonePlanner::new);

        BCRoboticsEntities.robotMap.values().forEach(robot -> EntityRenderers.register(robot.get(), RenderRobot::new));
    }

    // Calen 1.20.1
    private static final List<Runnable> spriteTasks = Lists.newLinkedList();

    // Calen 1.20.1
    @SubscribeEvent
    public static void onTextureStitchEvent$Post(TextureAtlasStitchedEvent event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            spriteTasks.forEach(Runnable::run);
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        PluggableRobotStation.setModelVariablesForItem();
        putModel(event, "robot_station#inventory", new ModelPluggableItem(spriteTasks::add, new LazyLoadedValue<>(() -> ROBOT_STATION_STATIC.getCutoutQuads()), new LazyLoadedValue<>(() -> ROBOT_STATION_DYNAMIC.getCutoutQuads())));

        PlugRobotStationRenderer.onModelBake();
    }

    private static void putModel(ModelEvent.ModifyBakingResult event, String str, BakedModel model) {
        event.getModels().replace(BCModules.ROBOTICS.createModelLocation(str), model);
    }
}
