package buildcraft.datagen.base;

import buildcraft.api.BCModules;
import buildcraft.lib.BCLibEventDistModBus;
import buildcraft.transport.BCTransportSprites;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BCSpriteSourceProvider extends SpriteSourceProvider {
    public BCSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, BCModules.BUILDCRAFT, fileHelper);
    }

    @Override
    protected void gather() {
        BCTransportSprites.onDatagenTextureRegister(this::addToBlockAtlas);
        BCLibEventDistModBus.onDatagenTextureRegister(this::addToBlockAtlas, existingFileHelper);
    }

    protected void addToBlockAtlas(ResourceLocation spriteLocation) {
        SourceList atlas = atlas(BLOCKS_ATLAS);
        atlas.addSource(new SingleFile(spriteLocation, Optional.empty()));
    }
}
