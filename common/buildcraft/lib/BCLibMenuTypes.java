package buildcraft.lib;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import buildcraft.lib.container.ContainerGuide;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class BCLibMenuTypes {
    public static final MenuType<ContainerGuide> GUIDE = IMenuTypeExtension.create((windowId, inv, data) ->
            {
                if (inv.player.getMainHandItem().getItem() == BCLibItems.guide.get() || inv.player.getOffhandItem().getItem() == BCLibItems.guide.get()) {
                    return new ContainerGuide(BCLibMenuTypes.GUIDE, windowId);
                } else {
                    return null;
                }
            }
    );

    public static void registerAll(RegisterEvent event) {
        event.register(Registries.MENU, ResourceLocation.fromNamespaceAndPath("buildcraftlib", "guide"), () -> GUIDE);
//        MenuScreens.register(
//                GUIDE,
//                (container, inv, title) ->
//                {
//                    Player player = inv.player;
//                    ItemStack stack;
//                    if (player.getMainHandItem().getItem() == BCLibItems.guide.get())
//                    {
//                        stack = player.getMainHandItem();
//                    }
//                    else if (player.getOffhandItem().getItem() == BCLibItems.guide.get())
//                    {
//                        stack = player.getOffhandItem();
//                    }
//                    else
//                    {
//                        stack = StackUtil.EMPTY;
//                    }
//                    String name = ItemGuide.getBookName(stack);
//                    if (name == null || name.isEmpty())
//                    {
//                        return new GuiGuide(container, title);
//                    }
//                    else
//                    {
//                        return new GuiGuide(container, name, title);
//                    }
//                }
//        );
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GUIDE, BCLibScreenConstructors.GUIDE);
    }
}
