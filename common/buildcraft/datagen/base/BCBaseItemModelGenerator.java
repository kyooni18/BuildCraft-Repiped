package buildcraft.datagen.base;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class BCBaseItemModelGenerator extends ItemModelProvider {
    protected static final ResourceLocation GENERATED = ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated");
    protected static final ResourceLocation CUBE_ALL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/cube_all");
    protected static final ResourceLocation HANDHELD = ResourceLocation.fromNamespaceAndPath("minecraft", "item/handheld");
    protected static final ResourceLocation BLOCK = ResourceLocation.fromNamespaceAndPath("minecraft", "block/block");
    protected static final ModelFile BUILTIN_ENTITY = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("minecraft", "builtin/entity"));

    public BCBaseItemModelGenerator(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }
}
