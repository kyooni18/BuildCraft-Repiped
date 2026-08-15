package buildcraft.energy;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.energy.client.gui.GuiDynamoMJ;
import buildcraft.energy.client.gui.GuiEngineIron_BC8;
import buildcraft.energy.client.gui.GuiEngineRF;
import buildcraft.energy.client.gui.GuiEngineStone_BC8;
import buildcraft.energy.container.ContainerDynamoMJ;
import buildcraft.energy.container.ContainerEngineIron_BC8;
import buildcraft.energy.container.ContainerEngineRF;
import buildcraft.energy.container.ContainerEngineStone_BC8;
import buildcraft.energy.tile.TileDynamoMJ;
import buildcraft.energy.tile.TileEngineIron_BC8;
import buildcraft.energy.tile.TileEngineRF;
import buildcraft.energy.tile.TileEngineStone_BC8;
import buildcraft.lib.misc.MessageUtil;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class BCEnergyMenuTypes {
    public static final MenuType<ContainerEngineIron_BC8> ENGINE_IRON = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileEngineIron_BC8 tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerEngineIron_BC8(BCEnergyMenuTypes.ENGINE_IRON, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerEngineStone_BC8> ENGINE_STONE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileEngineStone_BC8 tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerEngineStone_BC8(BCEnergyMenuTypes.ENGINE_STONE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerEngineRF> ENGINE_RF = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileEngineRF tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerEngineRF(BCEnergyMenuTypes.ENGINE_STONE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerDynamoMJ> DYNAMO_MJ = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileDynamoMJ tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerDynamoMJ(BCEnergyMenuTypes.ENGINE_STONE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftenergy", "engine_iron"), () -> ENGINE_IRON);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftenergy", "engine_stone"), () -> ENGINE_STONE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftenergy", "engine_rf"), () -> ENGINE_RF);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftenergy", "dynamo_mj"), () -> DYNAMO_MJ);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ENGINE_IRON, GuiEngineIron_BC8::new);
        event.register(ENGINE_STONE, GuiEngineStone_BC8::new);
        event.register(ENGINE_RF, GuiEngineRF::new);
        event.register(DYNAMO_MJ, GuiDynamoMJ::new);
    }
}
