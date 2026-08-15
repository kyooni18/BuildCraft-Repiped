/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.misc;

import buildcraft.api.core.IFluidFilter;
import buildcraft.api.core.IFluidHandlerAdv;
import buildcraft.lib.fluid.Tank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import buildcraft.api.compat.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidUtilBC {

    public static void pushFluidAround(LevelAccessor world, BlockPos pos, Tank tank) {
        FluidStack potential = tank.drain(tank.getFluidAmount(), FluidAction.SIMULATE);
        int drained = 0;
        if (potential == null || potential.isEmpty() || potential.getAmount() <= 0) {
            return;
        }
        FluidStack working = potential.copy();
        for (Direction side : Direction.values()) {
            if (potential.getAmount() <= 0) {
                break;
            }
            BlockEntity target = world.getBlockEntity(pos.relative(side));
            if (target == null) {
                continue;
            }
            IFluidHandler handler = CapUtil.getCapability(target, CapUtil.CAP_FLUIDS, side.getOpposite()).orElse(null);
            if (handler != null) {
                int used = handler.fill(potential.copy(), FluidAction.EXECUTE);

                if (used > 0) {
                    drained += used;
                    potential.setAmount(potential.getAmount() - used);
                }
            }
        }
        if (drained > 0) {
            FluidStack actuallyDrained = tank.drain(drained, FluidAction.EXECUTE);
//            if (actuallyDrained == null || actuallyDrained.getAmount() != drained)
            if (actuallyDrained == null || actuallyDrained.isEmpty() || actuallyDrained.getAmount() != drained) {
                String strWorking = StringUtilBC.fluidToString(working);
                String strActual = StringUtilBC.fluidToString(actuallyDrained);
                throw new IllegalStateException("Bad tank! Could drain " + strWorking + " but only drained " + strActual
                        + "( tank " + tank.getClass() + ")");
            }
        }
    }

    public static List<FluidStack> mergeSameFluids(List<FluidStack> fluids) {
        List<FluidStack> stacks = new ArrayList<>();
        fluids.forEach(toAdd ->
        {
            boolean found = false;
            for (FluidStack stack : stacks) {
                if (FluidStack.isSameFluidSameComponents(stack, toAdd)) {
                    stack.setAmount(stack.getAmount() + toAdd.getAmount());
                    found = true;
                }
            }
            if (!found) {
                stacks.add(toAdd.copy());
            }
        });
        return stacks;
    }

    public static boolean areFluidStackEqual(FluidStack a, FluidStack b) {
        return (a == null && b == null) || (a != null && b != null && FluidStack.isSameFluidSameComponents(a, b) && a.getAmount() == b.getAmount());
    }

    // Calen: use areFluidsEqualIgnoringStillOrFlow in 1.18.2
    @Deprecated(forRemoval = true)
    public static boolean areFluidsEqual(Fluid a, Fluid b) {
        if (a == null || b == null) {
            return a == b;
        }
        return getRegistryName(a).toString().equals(getRegistryName(b).toString());
    }

    // Calen
    public static boolean areFluidsEqualIgnoringStillOrFlow(Fluid a, Fluid b) {
        if (a == null || b == null) {
            return a == b;
        }
        return a == b || a.getFluidType() == b.getFluidType();
    }

    /** @return The fluidstack that was moved, or null if no fluid was moved. */
    @Nullable
    public static FluidStack move(IFluidHandler from, IFluidHandler to) {
        return move(from, to, Integer.MAX_VALUE);
    }

    /**
     * @param max The maximum amount of fluid to move.
     * @return The fluidstack that was moved, or null if no fluid was moved.
     */
    @Nullable
    public static FluidStack move(IFluidHandler from, IFluidHandler to, int max) {
        if (from == null || to == null) {
//            return null;
            return StackUtil.EMPTY_FLUID;
        }
        FluidStack toDrainPotential;
        if (from instanceof IFluidHandlerAdv) {
            IFluidFilter filter = f -> to.fill(f, FluidAction.SIMULATE) > 0;
            toDrainPotential = ((IFluidHandlerAdv) from).drain(filter, max, FluidAction.SIMULATE);
        } else {
            toDrainPotential = from.drain(max, FluidAction.SIMULATE);
        }
//        if (toDrainPotential == null)
        if (toDrainPotential.isEmpty()) {
            return StackUtil.EMPTY_FLUID;
        }
        int accepted = to.fill(toDrainPotential.copy(), FluidAction.SIMULATE);
        if (accepted <= 0) {
//            return null;
            return StackUtil.EMPTY_FLUID;
        }
        FluidStack toDrain = toDrainPotential.copyWithAmount(accepted);
        if (accepted < toDrainPotential.getAmount()) {
            toDrainPotential = from.drain(toDrain, FluidAction.SIMULATE);
//            if (toDrainPotential == null || toDrainPotential.getAmount() < accepted)
            if (toDrainPotential.isEmpty() || toDrainPotential.getAmount() < accepted) {
//                return null;
                return StackUtil.EMPTY_FLUID;
            }
        }
        FluidStack drained = from.drain(toDrain.copy(), FluidAction.EXECUTE);
//        if (drained == null || toDrain.getAmount() != drained.getAmount() || !FluidStack.isSameFluidSameComponents(toDrain, drained))
        if (drained.isEmpty() || toDrain.getAmount() != drained.getAmount() || !FluidStack.isSameFluidSameComponents(toDrain, drained)) {
            String detail = "(To Drain = " + StringUtilBC.fluidToString(toDrain);
            detail += ",\npotential drain = " + StringUtilBC.fluidToString(toDrainPotential) + ")";
            detail += ",\nactually drained = " + StringUtilBC.fluidToString(drained) + ")";
            detail += ",\nIFluidHandler (from) = " + from.getClass() + "(" + from + ")";
            detail += ",\nIFluidHandler (to) = " + to.getClass() + "(" + to + ")";
            throw new IllegalStateException("Drained fluid did not equal expected fluid!\n" + detail);
        }
        int actuallyAccepted = to.fill(drained, FluidAction.EXECUTE);
        if (actuallyAccepted != accepted) {
            String detail = "(actually accepted = " + actuallyAccepted + ", accepted = " + accepted + ")";
            throw new IllegalStateException("Mismatched IFluidHandler implementations!\n" + detail);
        }
        return drained.copyWithAmount(accepted);
    }

    public static InteractionResult onTankActivated(Player player, BlockPos pos, InteractionHand hand, IFluidHandler fluidHandler) {
        Level world = player.level();
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            return InteractionResult.PASS;
        }
        boolean replace = !player.isCreative();
        boolean single = held.getCount() == 1;
        IFluidHandlerItem flItem = null;
        if (replace && single) {
            flItem = FluidUtil.getFluidHandler(held).orElse(null);
        } else {
            // replace and not single - need a copy and count set to 1
            // not replace and single - need a copy, does not need change of count but it should be ok
            // not replace and not single - need a copy count set to 1
            ItemStack copy = held.copy();
            copy.setCount(1);
            flItem = FluidUtil.getFluidHandler(copy).orElse(null);
        }
        if (flItem == null) {
            return InteractionResult.PASS;
        }
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        boolean changed = true;
        FluidStack moved;
//        if ((moved = FluidUtilBC.move(flItem, fluidHandler)) != null)
        if (!(moved = FluidUtilBC.move(flItem, fluidHandler)).isEmpty()) {
            SoundUtil.playBucketEmpty(world, pos, moved);
        }
//        else if ((moved = FluidUtilBC.move(fluidHandler, flItem)) != null)
        else if (!(moved = FluidUtilBC.move(fluidHandler, flItem)).isEmpty()) {
            SoundUtil.playBucketFill(world, pos, moved);
        } else {
            changed = false;
        }

        if (changed && replace) {
            if (single) {
                // if it was the single item, replace with changed one
                player.setItemInHand(hand, flItem.getContainer());
            } else {
                // if it was part of stack, shrink stack and give / drop the new one
                held.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, flItem.getContainer());
            }
//            player.inventoryContainer.detectAndSendChanges();
            player.inventoryMenu.broadcastChanges();
        }
        return InteractionResult.SUCCESS;
    }

    // Calen
    public static Fluid getItemFromRegistryName(String name) {
        return getItemFromRegistryName(ResourceLocation.parse(name));
    }

    public static Fluid getItemFromRegistryName(ResourceLocation name) {
        return ForgeRegistries.FLUIDS.getValue(name);
    }

    public static ResourceLocation getRegistryName(Fluid fluid) {
        return fluid.builtInRegistryHolder().key().location();
    }

    public static ResourceLocation getStillTexture(Fluid fluid) {
        return IClientFluidTypeExtensions.of(fluid).getStillTexture();
    }

    public static ResourceLocation getFlowingTexture(Fluid fluid) {
        return IClientFluidTypeExtensions.of(fluid).getFlowingTexture();
    }

    public static int getColor(Fluid fluid) {
        return IClientFluidTypeExtensions.of(fluid).getTintColor();
    }
}
