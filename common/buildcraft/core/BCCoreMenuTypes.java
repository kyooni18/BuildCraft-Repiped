package buildcraft.core;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.core.list.ContainerList;
import buildcraft.core.list.GuiList;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class BCCoreMenuTypes {
    public static final MenuType<ContainerList> LIST = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                return new ContainerList(BCCoreMenuTypes.LIST, windowId, inv.player);
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftcore", "list"), () -> LIST);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(LIST, GuiList::new);
    }
}
