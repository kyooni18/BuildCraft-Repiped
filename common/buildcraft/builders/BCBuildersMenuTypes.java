package buildcraft.builders;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.builders.container.*;
import buildcraft.builders.gui.*;
import buildcraft.builders.tile.*;
import buildcraft.lib.misc.MessageUtil;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class BCBuildersMenuTypes {
    public static final MenuType<ContainerBuilder> BUILDER = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileBuilder tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerBuilder(BCBuildersMenuTypes.BUILDER, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerArchitectTable> ARCHITECT_TABLE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileArchitectTable tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerArchitectTable(BCBuildersMenuTypes.ARCHITECT_TABLE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerElectronicLibrary> ELECTRONIC_LIBRARY = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileElectronicLibrary tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerElectronicLibrary(BCBuildersMenuTypes.ELECTRONIC_LIBRARY, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerReplacer> REPLACER = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileReplacer tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerReplacer(BCBuildersMenuTypes.REPLACER, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerFiller> FILLER = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileFiller tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerFiller(BCBuildersMenuTypes.FILLER, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerFillerPlanner> FILLER_PLANNER = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                return new ContainerFillerPlanner(BCBuildersMenuTypes.FILLER_PLANNER, windowId, inv.player);
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftbuilders", "builder"), () -> BUILDER);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftbuilders", "architect_table"), () -> ARCHITECT_TABLE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftbuilders", "electronic_library"), () -> ELECTRONIC_LIBRARY);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftbuilders", "replacer"), () -> REPLACER);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftbuilders", "filler"), () -> FILLER);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftbuilders", "filler_planner"), () -> FILLER_PLANNER);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BUILDER, GuiBuilder::new);
        event.register(ARCHITECT_TABLE, GuiArchitectTable::new);
        event.register(ELECTRONIC_LIBRARY, GuiElectronicLibrary::new);
        event.register(REPLACER, GuiReplacer::new);
        event.register(FILLER, GuiFiller::new);
        event.register(FILLER_PLANNER, GuiFillerPlanner::new);
    }
}
