package buildcraft.silicon;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.api.net.IMessage;
import buildcraft.api.transport.pipe.IPipeHolder;
import buildcraft.lib.misc.MessageUtil;
import buildcraft.lib.tile.TileBC_Neptune;
import buildcraft.silicon.container.*;
import buildcraft.silicon.gate.GateLogic;
import buildcraft.silicon.gui.*;
import buildcraft.silicon.plug.PluggableGate;
import buildcraft.silicon.tile.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

/** The static fields are used to create GUI on client thread. */
public class BCSiliconMenuTypes {
    public static final MenuType<ContainerAssemblyTable> ASSEMBLY_TABLE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileAssemblyTable tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerAssemblyTable(BCSiliconMenuTypes.ASSEMBLY_TABLE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerIntegrationTable> INTEGRATION_TABLE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileIntegrationTable tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerIntegrationTable(BCSiliconMenuTypes.INTEGRATION_TABLE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerAdvancedCraftingTable> ADVANCED_CRAFTING_TABLE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileAdvancedCraftingTable tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerAdvancedCraftingTable(BCSiliconMenuTypes.INTEGRATION_TABLE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerChargingTable> CHARGING_TABLE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileChargingTable tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerChargingTable(BCSiliconMenuTypes.CHARGING_TABLE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    public static final MenuType<ContainerProgrammingTable_Neptune> PROGRAMMING_TABLE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.level().getBlockEntity(data.readBlockPos()) instanceof TileProgrammingTable_Neptune tile) {
                    MessageUtil.clientHandleUpdateTileMsgBeforeOpen(tile, data);
                    return new ContainerProgrammingTable_Neptune(BCSiliconMenuTypes.PROGRAMMING_TABLE, windowId, inv.player, tile);
                } else {
                    return null;
                }
            }
    );
    /**
     * {@link IPipeHolder#onPlayerOpen(Player)} is moved from {@link ContainerGate#ContainerGate(MenuType, int, Player, GateLogic)} in 1.12.2
     * to ensure the new gate obj created before GUI opened.
     * <p>
     * {@link IPipeHolder#getPluggable(Direction)} is used to receive the new gate obj.
     * <p>
     * {@link MessageUtil#clientHandleUpdateTileMsgBeforeOpen(TileBC_Neptune, FriendlyByteBuf, Runnable...)}
     * handles the message created in {@link MessageUtil#serverOpenGUIWithMsg(Player, MenuProvider, BlockPos, int, IMessage)}
     * in {@link PluggableGate#onPluggableActivate(Player, HitResult, float, float, float)}
     */
    public static final MenuType<ContainerGate> GATE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                BlockPos pos = data.readBlockPos();
                if (inv.player.level().getBlockEntity(pos) instanceof IPipeHolder holder) {
                    int id = data.readInt();
                    Direction direction = Direction.from3DDataValue(id >>> 8);
                    if (holder.getPluggable(direction) instanceof PluggableGate gate) {
                        MessageUtil.clientHandleUpdateTileMsgBeforeOpen((TileBC_Neptune) holder, data);
                        gate.logic.getPipeHolder().onPlayerOpen(inv.player);

                        // Refresh the gate object
                        gate = (PluggableGate) holder.getPluggable(direction);

                        return new ContainerGate(BCSiliconMenuTypes.GATE, windowId, inv.player, gate.logic);
                    }
                }
                return null;
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftsilicon", "assembly_table"), () -> ASSEMBLY_TABLE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftsilicon", "integration_table"), () -> INTEGRATION_TABLE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftsilicon", "advanced_crafting_table"), () -> ADVANCED_CRAFTING_TABLE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftsilicon", "charging_table"), () -> CHARGING_TABLE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftsilicon", "programming_table"), () -> PROGRAMMING_TABLE);
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftsilicon", "gate"), () -> GATE);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ASSEMBLY_TABLE, GuiAssemblyTable::new);
        event.register(INTEGRATION_TABLE, GuiIntegrationTable::new);
        event.register(ADVANCED_CRAFTING_TABLE, GuiAdvancedCraftingTable::new);
        event.register(CHARGING_TABLE, GuiChargingTable::new);
        event.register(PROGRAMMING_TABLE, GuiProgrammingTable_Neptune::new);
        event.register(GATE, GuiGate::new);
    }
}
