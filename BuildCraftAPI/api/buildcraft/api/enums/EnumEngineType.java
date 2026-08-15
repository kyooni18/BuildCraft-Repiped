package buildcraft.api.enums;

import buildcraft.api.core.IEngineType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum EnumEngineType implements StringRepresentable, IEngineType {
    WOOD("core", "wood"),
    STONE("core", "stone"),
    IRON("core", "iron"),
    CREATIVE("core", "creative"),
    RF("core", "rf"),
    ;

    // public final String unlocalizedTag;
    private final String unlocalizedTag;
    // public final String resourceLocation;
    public final ResourceLocation resourceLocation;

    public static final EnumEngineType[] VALUES = values();

    EnumEngineType(String mod, String loc) {
        unlocalizedTag = loc;
        // resourceLocation = "buildcraft" + mod + ":blocks/engine/inv/" + loc;
        resourceLocation = ResourceLocation.fromNamespaceAndPath("buildcraft" + mod, "engine_" + loc);
    }

    @Override
    // public String getItemModelLocation()
    public ResourceLocation getItemModelLocation() {
        return resourceLocation;
    }

    @Override
    public String getSerializedName() {
        return unlocalizedTag;
    }

//    public static EnumEngineType fromMeta(int meta) {
//        if (meta < 0 || meta >= VALUES.length) {
//            meta = 0;
//        }
//        return VALUES[meta];
//    }
}
