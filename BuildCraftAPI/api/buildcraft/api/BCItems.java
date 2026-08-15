package buildcraft.api;

import buildcraft.api.items.FluidItemDrops;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BCItems {

    public static class Lib {
        public static Item GUIDE;
        public static Item GUIDE_NOTE;
        public static Item DEBUGGER;
    }

    public static class Core {
        public static Item GEAR_WOOD;
        public static Item GEAR_STONE;
        public static Item GEAR_IRON;
        public static Item GEAR_GOLD;
        public static Item GEAR_DIAMOND;
        public static Item WRENCH;
        public static Item PAINTBRUSH_CLEAN;
        public static Item LIST;
        public static Item MAP_LOCATION;
        public static Item MARKER_CONNECTOR;
        public static Item VOLUME_BOX;
        public static Item GOGGLES;

        /**
         * It is recommended that you refer to {@link FluidItemDrops#item} when creating fluid drops rather than
         * this.
         */
        public static Item FRAGILE_FLUID_SHARD;
    }

    public static class Builders {
//        @ObjectHolder(registryName = "item", value = "buildcraftbuilders:")
    }

    public static class Energy {
        public static Item GLOB_OIL;
    }

    public static class Factory {
        public static Item PLASTIC_SHEET;
        public static Item WATER_GEL_SPAWN;
        public static Item GEL;
    }

    public static class Transport {
        public static Item PLUG_BLOCKER;
        public static Item PLUG_POWER_ADAPTOR;

        public static Item PIPE_STRUCTURE_COBBLESTONE_COLORLESS;
        public static Item PIPE_ITEMS_WOOD_COLORLESS;
        public static Item PIPE_ITEMS_EMZULI_COLORLESS;
        public static Item PIPE_ITEMS_DIAMOND_WOOD_COLORLESS;
        public static Item PIPE_FLUIDS_WOOD_COLORLESS;
        public static Item PIPE_FLUIDS_DIAMOND_WOOD_COLORLESS;
    }

    public static class Silicon {
        public static Item CHIPSET_REDSTONE;
        public static Item CHIPSET_IRON;
        public static Item CHIPSET_GOLD;
        public static Item CHIPSET_QUARTZ;
        public static Item CHIPSET_DIAMOND;

        public static Item PLUG_PULSAR;
        public static Item PLUG_FACADE;
    }

    public static class Robotics {
//        @ObjectHolder(registryName = "item", value = "buildcraftrobotics:")
    }

    /** Populates legacy API fields after NeoForge registry binding has completed. */
    public static void init() {
        Lib.GUIDE = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftlib:guide")).orElse(null);
        Lib.GUIDE_NOTE = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftlib:guide_note")).orElse(null);
        Lib.DEBUGGER = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftlib:debugger")).orElse(null);
        Core.GEAR_WOOD = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:gear_wood")).orElse(null);
        Core.GEAR_STONE = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:gear_stone")).orElse(null);
        Core.GEAR_IRON = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:gear_iron")).orElse(null);
        Core.GEAR_GOLD = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:gear_gold")).orElse(null);
        Core.GEAR_DIAMOND = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:gear_diamond")).orElse(null);
        Core.WRENCH = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:wrench")).orElse(null);
        Core.PAINTBRUSH_CLEAN = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:paintbrush_clean")).orElse(null);
        Core.LIST = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:list")).orElse(null);
        Core.MAP_LOCATION = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:map_location")).orElse(null);
        Core.MARKER_CONNECTOR = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:marker_connector")).orElse(null);
        Core.VOLUME_BOX = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:volume_box")).orElse(null);
        Core.GOGGLES = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:goggles")).orElse(null);
        Core.FRAGILE_FLUID_SHARD = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftcore:fragile_fluid_shard")).orElse(null);
        Energy.GLOB_OIL = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftenergy:glob_oil")).orElse(null);
        Factory.PLASTIC_SHEET = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftfactory:plastic_sheet")).orElse(null);
        Factory.WATER_GEL_SPAWN = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftfactory:water_gel_spawn")).orElse(null);
        Factory.GEL = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftfactory:gel")).orElse(null);
        Transport.PLUG_BLOCKER = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:plug_blocker")).orElse(null);
        Transport.PLUG_POWER_ADAPTOR = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:plug_power_adaptor")).orElse(null);
        Transport.PIPE_STRUCTURE_COBBLESTONE_COLORLESS = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_structure_cobblestone_colorless")).orElse(null);
        Transport.PIPE_ITEMS_WOOD_COLORLESS = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_items_wood_colorless")).orElse(null);
        Transport.PIPE_ITEMS_EMZULI_COLORLESS = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_items_emzuli_colorless")).orElse(null);
        Transport.PIPE_ITEMS_DIAMOND_WOOD_COLORLESS = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_items_diamond_wood_colorless")).orElse(null);
        Transport.PIPE_FLUIDS_WOOD_COLORLESS = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_fluids_wood_colorless")).orElse(null);
        Transport.PIPE_FLUIDS_DIAMOND_WOOD_COLORLESS = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcrafttransport:pipe_fluids_diamond_wood_colorless")).orElse(null);
        Silicon.CHIPSET_REDSTONE = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:chipset_redstone")).orElse(null);
        Silicon.CHIPSET_IRON = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:chipset_iron")).orElse(null);
        Silicon.CHIPSET_GOLD = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:chipset_gold")).orElse(null);
        Silicon.CHIPSET_QUARTZ = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:chipset_quartz")).orElse(null);
        Silicon.CHIPSET_DIAMOND = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:chipset_diamond")).orElse(null);
        Silicon.PLUG_PULSAR = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:plug_pulsar")).orElse(null);
        Silicon.PLUG_FACADE = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse("buildcraftsilicon:plug_facade")).orElse(null);
    }
}
