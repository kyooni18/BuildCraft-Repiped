package buildcraft.robotics;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.lib.misc.MessageUtil;
import buildcraft.robotics.container.ContainerRequester;
import buildcraft.robotics.container.ContainerZonePlanner;
import buildcraft.robotics.gui.GuiRequester;
import buildcraft.robotics.gui.GuiZonePlanner;
import buildcraft.robotics.tile.TileRequester;
import buildcraft.robotics.tile.TileZonePlanner;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class BCRoboticsMenuTypes {
    public static final MenuType<ContainerZonePlanner> ZONE_PLANNER = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileZonePlanner tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerZonePlanner(BCRoboticsMenuTypes.ZONE_PLANNER, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerRequester> REQUESTER = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileRequester tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerRequester(BCRoboticsMenuTypes.REQUESTER, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftrobotics", "zone_planner"), () -> ZONE_PLANNER);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftrobotics", "requester"), () -> REQUESTER);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ZONE_PLANNER, GuiZonePlanner::new);
        event.register(REQUESTER, GuiRequester::new);
    }
}
