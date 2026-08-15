package buildcraft.lib.fluid;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.Map;

/**
 * The methods are just copied from the super classes and changed some vars
 * to make oils behave like they did in 1.12.2.
 */
public abstract class BCFluid extends BaseFlowingFluid {
    protected BCFluidRegistryContainer fluidRegistryContainer;
    protected boolean isGas;

    protected BCFluid(BaseFlowingFluid.Properties properties, BCFluidRegistryContainer reg) {
        super(properties);
        this.fluidRegistryContainer = reg;
        this.isGas = reg.isLighterThanAir();
    }

    public BCFluidRegistryContainer getReg() {
        return fluidRegistryContainer;
    }

    // Flow & Still

    public static class Flowing extends BCFluid {
        public Flowing(Properties properties, BCFluidRegistryContainer reg) {
            super(properties, reg);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends BCFluid {
        public Source(Properties properties, BCFluidRegistryContainer reg) {
            super(properties, reg);
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    // BaseFlowingFluid

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction) {
        return false; // never be replaced by other fluids
    }

    // FlowingFluid

    /**
     * To make gas spread up.
     *
     * @param world
     * @param pos
     * @param state
     */
    @Override
    protected void spread(Level world, BlockPos pos, FluidState state) {
        super.spread(world, pos, state);
    }

    @Override
    protected FluidState getNewLiquid(Level world, BlockPos pos, BlockState state) {
        return super.getNewLiquid(world, pos, state);
    }

    @Override
    public Vec3 getFlow(BlockGetter p_75987_, BlockPos p_75988_, FluidState p_75989_) {
        return super.getFlow(p_75987_, p_75988_, p_75989_);
    }

    @Override
    protected int getSlopeDistance(LevelReader p_76027_, BlockPos p_76028_, int p_76029_, Direction p_76030_, BlockState p_76031_, BlockPos p_76032_, Short2ObjectMap<Pair<BlockState, FluidState>> p_76033_, Short2BooleanMap p_76034_) {
        return super.getSlopeDistance(p_76027_, p_76028_, p_76029_, p_76030_, p_76031_, p_76032_, p_76033_, p_76034_);
    }

    @Override
    protected Map<Direction, FluidState> getSpread(Level p_256191_, BlockPos p_76081_, BlockState p_76082_) {
        return super.getSpread(p_256191_, p_76081_, p_76082_);
    }

    @Override
    protected boolean isSolidFace(BlockGetter p_75991_, BlockPos p_75992_, Direction p_75993_) {
        BlockState blockstate = p_75991_.getBlockState(p_75992_);
        FluidState fluidstate = p_75991_.getFluidState(p_75992_);
        if (fluidstate.getType().isSame(this)) {
            return false;
        } else if (p_75993_ == /*here different from FlowingFluid*/ (isGas ? Direction.DOWN : Direction.UP)) {
            return true;
        } else {
            return blockstate.getBlock() instanceof IceBlock ? false : blockstate.isFaceSturdy(p_75991_, p_75992_, p_75993_);
        }
    }

    // @Override
    protected boolean isWaterHole(BlockGetter p_75957_, Fluid p_75958_, BlockPos p_75959_, BlockState p_75960_, BlockPos p_75961_, BlockState p_75962_) {
        return p_75962_.getFluidState().getType().isSame(this) || this.canHoldFluid(p_75957_, p_75961_, p_75962_, p_75958_);
    }

    /** To protect water block */
    protected boolean canHoldFluid(BlockGetter p_75973_, BlockPos p_75974_, BlockState p_75975_, Fluid p_75976_) {
        // Calen: for oil spread on water and not replace water
        if (p_75973_.getFluidState(p_75974_).is(FluidTags.WATER)) {
            return false;
        }

        Block block = p_75975_.getBlock();
        if (block instanceof LiquidBlockContainer) {
            return ((LiquidBlockContainer) block).canPlaceLiquid(null, p_75973_, p_75974_, p_75975_, p_75976_);
        } else if (!(block instanceof DoorBlock) && !p_75975_.is(BlockTags.SIGNS) && !p_75975_.is(Blocks.LADDER) && !p_75975_.is(Blocks.SUGAR_CANE) && !p_75975_.is(Blocks.BUBBLE_COLUMN)) {
            if (!p_75975_.is(Blocks.NETHER_PORTAL) && !p_75975_.is(Blocks.END_PORTAL) && !p_75975_.is(Blocks.END_GATEWAY) && !p_75975_.is(Blocks.STRUCTURE_VOID)) {
                return !p_75975_.blocksMotion();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public VoxelShape getShape(FluidState p_76084_, BlockGetter p_76085_, BlockPos p_76086_) {
        boolean hasSameBelowOrAbove = /*here different from FlowingFluid*/ isGas ? hasSameBelow(p_76084_, p_76085_, p_76086_) : hasSameAbove(p_76084_, p_76085_, p_76086_);
        if (p_76084_.getAmount() == 9 && hasSameBelowOrAbove) {
            return Shapes.block();
        }
        if (isGas) {
            return Shapes.box(0.0D, 1.0D - p_76084_.getOwnHeight(), 0.0D, 1.0D, 1.0D, 1.0D);
        }
        return Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, (double) p_76084_.getHeight(p_76085_, p_76086_), 1.0D);
    }

    public float getBottomHeight(FluidState p_76050_, BlockGetter p_76051_, BlockPos p_76052_) {
        return hasSameBelow(p_76050_, p_76051_, p_76052_) ? 0.0F : 1.0F - p_76050_.getOwnHeight();
    }

    private static boolean hasSameBelow(FluidState p_76089_, BlockGetter p_76090_, BlockPos p_76091_) {
        return p_76089_.getType().isSame(p_76090_.getFluidState(p_76091_.below()).getType());
    }

    private static boolean hasSameAbove(FluidState p_76089_, BlockGetter p_76090_, BlockPos p_76091_) {
        return p_76089_.getType().isSame(p_76090_.getFluidState(p_76091_.above()).getType());
    }

    @Override
    public float getHeight(FluidState p_76050_, BlockGetter p_76051_, BlockPos p_76052_) {
        if (isGas) {
            return 1.0F;
        } else {
            return hasSameAbove(p_76050_, p_76051_, p_76052_) ? 1.0F : p_76050_.getOwnHeight();
        }
    }
}
