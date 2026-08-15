package buildcraft.api;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BCBlocks {

    public static class Core {
        public static Block SPRING_WATER;
        public static Block SPRING_OIL;
        public static Block DECORATED_DESTROY;
        public static Block DECORATED_BLUEPRINT;
        public static Block DECORATED_TEMPLATE;
        public static Block DECORATED_PAPER;
        public static Block DECORATED_LEATHER;
        public static Block DECORATED_LASER_BACK;
        public static Block ENGINE_WOOD;
        public static Block ENGINE_STONE;
        public static Block ENGINE_IRON;
        public static Block ENGINE_CREATIVE;
        public static Block MARKER_VOLUME;
        public static Block MARKER_PATH;
    }

    public static class Builders {
        public static Block ARCHITECT;
        public static Block BUILDER;
        public static Block FILLER;
        public static Block LIBRARY;
        public static Block REPLACER;
        public static Block QUARRY;
        public static Block FRAME;
    }

    public static class Energy {
        // Fluid blocks can be accessed ~somewhere else~
//        @ObjectHolder(registryName = "block", value = "buildcraftenergy:")
    }

    public static class Factory {
        public static Block AUTOWORKBENCH_ITEM;
        public static Block MINING_WELL;
        public static Block PUMP;
        public static Block TUBE;
        public static Block FLOOD_GATE;
        public static Block TANK;
        public static Block CHUTE;
        public static Block WATER_GEL;
        public static Block DISTILLER;
        public static Block HEAT_EXCHANGE;
    }

    public static class Transport {
        public static Block FILTERED_BUFFER;
        public static Block PIPE_HOLDER;
    }

    public static class Silicon {
        public static Block LASER;
        public static Block ASSEMBLY_TABLE;
        public static Block ADVANCED_CRAFTING_TABLE;
        public static Block INTEGRATION_TABLE;
        public static Block CHARGING_TABLE;
        public static Block PROGRAMMING_TABLE;
    }

    public static class Robotics {
//        @ObjectHolder(registryName = "block", value = "buildcraftrobotics:")
    }

    /** Populates legacy API fields after NeoForge registry binding has completed. */
    public static void init() {
        Core.SPRING_WATER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:spring_water")).orElse(null);
        Core.SPRING_OIL = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:spring_oil")).orElse(null);
        Core.DECORATED_DESTROY = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:decorated_destroy")).orElse(null);
        Core.DECORATED_BLUEPRINT = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:decorated_blueprint")).orElse(null);
        Core.DECORATED_TEMPLATE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:decorated_template")).orElse(null);
        Core.DECORATED_PAPER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:decorated_paper")).orElse(null);
        Core.DECORATED_LEATHER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:decorated_leather")).orElse(null);
        Core.DECORATED_LASER_BACK = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:decorated_laser_back")).orElse(null);
        Core.ENGINE_WOOD = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:engine_wood")).orElse(null);
        Core.ENGINE_STONE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:engine_stone")).orElse(null);
        Core.ENGINE_IRON = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:engine_iron")).orElse(null);
        Core.ENGINE_CREATIVE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:engine_creative")).orElse(null);
        Core.MARKER_VOLUME = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:marker_volume")).orElse(null);
        Core.MARKER_PATH = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftcore:marker_path")).orElse(null);
        Builders.ARCHITECT = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:architect")).orElse(null);
        Builders.BUILDER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:builder")).orElse(null);
        Builders.FILLER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:filler")).orElse(null);
        Builders.LIBRARY = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:library")).orElse(null);
        Builders.REPLACER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:replacer")).orElse(null);
        Builders.QUARRY = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:quarry")).orElse(null);
        Builders.FRAME = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftbuilders:frame")).orElse(null);
        Factory.AUTOWORKBENCH_ITEM = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:autoworkbench_item")).orElse(null);
        Factory.MINING_WELL = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:mining_well")).orElse(null);
        Factory.PUMP = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:pump")).orElse(null);
        Factory.TUBE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:tube")).orElse(null);
        Factory.FLOOD_GATE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:flood_gate")).orElse(null);
        Factory.TANK = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:tank")).orElse(null);
        Factory.CHUTE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:chute")).orElse(null);
        Factory.WATER_GEL = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:water_gel")).orElse(null);
        Factory.DISTILLER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:distiller")).orElse(null);
        Factory.HEAT_EXCHANGE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftfactory:heat_exchange")).orElse(null);
        Transport.FILTERED_BUFFER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcrafttransport:filtered_buffer")).orElse(null);
        Transport.PIPE_HOLDER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_holder")).orElse(null);
        Silicon.LASER = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftsilicon:laser")).orElse(null);
        Silicon.ASSEMBLY_TABLE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftsilicon:assembly_table")).orElse(null);
        Silicon.ADVANCED_CRAFTING_TABLE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftsilicon:advanced_crafting_table")).orElse(null);
        Silicon.INTEGRATION_TABLE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftsilicon:integration_table")).orElse(null);
        Silicon.CHARGING_TABLE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftsilicon:charging_table")).orElse(null);
        Silicon.PROGRAMMING_TABLE = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse("buildcraftsilicon:programming_table")).orElse(null);
    }
}
