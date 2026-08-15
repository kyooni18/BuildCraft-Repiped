package buildcraft.factory;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.factory.container.ContainerAutoCraftItems;
import buildcraft.factory.container.ContainerChute;
import buildcraft.factory.container.ContainerTank;
import buildcraft.factory.gui.GuiAutoCraftItems;
import buildcraft.factory.gui.GuiChute;
import buildcraft.factory.gui.GuiTank;
import buildcraft.factory.tile.TileAutoWorkbenchItems;
import buildcraft.factory.tile.TileChute;
import buildcraft.factory.tile.TileTank;
import buildcraft.lib.misc.MessageUtil;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class BCFactoryMenuTypes {
    public static final MenuType<ContainerChute> CHUTE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileChute tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerChute(BCFactoryMenuTypes.CHUTE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerAutoCraftItems> AUTO_WORKBENCH_ITEMS = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileAutoWorkbenchItems tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerAutoCraftItems(BCFactoryMenuTypes.AUTO_WORKBENCH_ITEMS, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerTank> TANK = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileTank tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerTank(BCFactoryMenuTypes.TANK, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftfactory", "chute"), () -> CHUTE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftfactory", "auto_workbench_items"), () -> AUTO_WORKBENCH_ITEMS);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftfactory", "tank"), () -> TANK);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CHUTE, GuiChute::new);
        event.register(AUTO_WORKBENCH_ITEMS, GuiAutoCraftItems::new);
        event.register(TANK, GuiTank::new);
    }
}
