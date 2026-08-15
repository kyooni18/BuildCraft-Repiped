package buildcraft.lib.fluid;

import buildcraft.lib.item.ItemBucketBC;
import buildcraft.lib.registry.RegistryObject;

import java.util.function.Supplier;

public class BCFluidRegistryContainer {
    private RegistryObject<BCFluid.Source> still;
    private RegistryObject<BCFluid.Flowing> flowing;
    private RegistryObject<BCFluidBlock> block;
    private RegistryObject<ItemBucketBC> bucket;
    private Supplier<BCFluidAttributes> fluidType;
    private boolean lighterThanAir;


    public BCFluid.Source getStill() {
        return still.get();
    }

    public BCFluid.Flowing getFlowing() {
        return flowing.get();
    }

    public BCFluidBlock getBlock() {
        return block.get();
    }

    public ItemBucketBC getBucket() {
        return bucket.get();
    }

    public BCFluidAttributes getFluidType() {
        return fluidType.get();
    }

    public boolean isLighterThanAir() {
        return lighterThanAir;
    }


    public void setStill(RegistryObject<BCFluid.Source> stillFluid) {
        this.still = stillFluid;
    }

    public void setFlowing(RegistryObject<BCFluid.Flowing> flowingFluid) {
        this.flowing = flowingFluid;
    }

    public void setBlock(RegistryObject<BCFluidBlock> block) {
        this.block = block;
    }

    public void setBucket(RegistryObject<ItemBucketBC> bucket) {
        this.bucket = bucket;
    }

    public void setFluidType(Supplier<BCFluidAttributes> fluidType) {
        this.fluidType = fluidType;
    }

    public void setLighterThanAir(boolean lighterThanAir) {
        this.lighterThanAir = lighterThanAir;
    }
}
