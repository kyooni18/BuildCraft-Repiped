package buildcraft.api.mj;

import net.minecraft.core.Direction;
import buildcraft.api.compat.capability.Capability;
import buildcraft.api.compat.capability.ForgeCapabilities;
import buildcraft.api.compat.capability.ICapabilityProvider;
import buildcraft.api.compat.LazyOptional;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Provides a quick way to return all types of a single {@link IMjConnector} for all the different capabilities. */
public class MjCapabilityHelper implements ICapabilityProvider {

    @Nonnull
    private final IMjConnector connector;

    @Nullable
    private final IMjReceiver receiver;

    @Nullable
    private final IMjRedstoneReceiver rsReceiver;

    @Nullable
    private final IMjReadable readable;

    @Nullable
    private final IMjPassiveProvider provider;

    @Nullable
    private final IEnergyStorage rfAutoConvert;

    public MjCapabilityHelper(@Nonnull IMjConnector mj) {
        this.connector = mj;
        this.receiver = mj instanceof IMjReceiver ? (IMjReceiver) mj : null;
        this.rsReceiver = mj instanceof IMjRedstoneReceiver ? (IMjRedstoneReceiver) mj : null;
        this.readable = mj instanceof IMjReadable ? (IMjReadable) mj : null;
        this.provider = mj instanceof IMjPassiveProvider ? (IMjPassiveProvider) mj : null;

        if (MjAPI.isRfAutoConversionEnabled()) {
            rfAutoConvert = new IEnergyStorage() {

                @Override
                public int getEnergyStored() {
                    IMjReadable read = readable;
                    if (read != null) {
                        long mjPerRf = MjAPI.getRfConversion().mjPerRf;
                        return (int) (read.getStored() / mjPerRf);
                    } else {
                        return 0;
                    }
                }

                @Override
                public int getMaxEnergyStored() {
                    IMjReadable read = readable;
                    if (read != null) {
                        long mjPerRf = MjAPI.getRfConversion().mjPerRf;
                        return (int) (read.getCapacity() / mjPerRf);
                    } else {
                        return 0;
                    }
                }

                @Override
                public boolean canReceive() {
                    return receiver != null && receiver.canReceive();
                }

                /** @return Amount of energy that was (or would have been, if simulated) accepted by the storage. */
                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {

                    if (maxReceive <= 0) {
                        return 0;
                    }

                    IMjReceiver recv = receiver;
                    if (recv == null || !recv.canReceive()) {
                        return 0;
                    }

                    long mjPerRf = MjAPI.getRfConversion().mjPerRf;
                    long maxReceiveMj = maxReceive * mjPerRf;
                    long excess = recv.receivePower(maxReceiveMj, true);

                    // Actual MJ that was accepted
                    long acceptedMj = maxReceiveMj - excess;

                    if (acceptedMj < mjPerRf) {
                        return 0;
                    }

                    // MJ that was accepted but cannot be converted back to RF
                    // (We need to actual accepted MJ to be some integer multiple of mjPerRf)
                    long excessMj = acceptedMj % mjPerRf;
                    // An MJ value that is an integer multiple of mjPerRf
                    // Calen FIX: acceptedMj, not maxReceiveMj
                    // long exactAcceptableMj = maxReceiveMj - excessMj;
                    long exactAcceptableMj = acceptedMj - excessMj;

                    if (exactAcceptableMj <= 0) {
                        return 0;
                    }

                    int rf = (int) (exactAcceptableMj / mjPerRf);
                    if (rf * mjPerRf != exactAcceptableMj) {
                        // Sanity check
                        throw new IllegalStateException(
                                "Programmer made a mistake?? mjPerRf=" + mjPerRf + ", rf=" + rf + ", exactAcceptableMJ="
                                        + exactAcceptableMj
                        );
                    }

                    long excess2 = recv.receivePower(exactAcceptableMj, true);

                    if (excess2 != 0) {
                        // Odd. This means we can't actually accept the exact amount
                        // not actually a crash
                        return 0;
                    }

                    if (!simulate) {
                        long excess3 = recv.receivePower(exactAcceptableMj, simulate);

                        if (excess3 != excess2) {
                            throw new IllegalStateException("Bad impl: " + recv.getClass() + " of receivePower");
                        }
                    }

                    return rf;
                }

                @Override
                public boolean canExtract() {
                    return provider != null;
                }

                /** @return Amount of energy that was (or would have been, if simulated) extracted from the storage. */
                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    long mjPerRf = MjAPI.getRfConversion().mjPerRf;
                    // TODO!
                    // (Nothing in buildcraft supports this at the moment)
                    return 0;
                }

            };
        } else {
            rfAutoConvert = null;
        }
    }

//    @Override
//    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
//        return getCapability(capability, facing) != null;
//    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
        if (capability == MjAPI.CAP_CONNECTOR) {
//            return MjAPI.CAP_CONNECTOR.cast(connector);
            return connector == null ? LazyOptional.empty() : LazyOptional.of(() -> connector).cast();
        }
        if (capability == MjAPI.CAP_RECEIVER) {
//            return MjAPI.CAP_RECEIVER.cast(receiver);
            return receiver == null ? LazyOptional.empty() : LazyOptional.of(() -> receiver).cast();
        }
        if (capability == MjAPI.CAP_REDSTONE_RECEIVER) {
//            return MjAPI.CAP_REDSTONE_RECEIVER.cast(rsReceiver);
            return rsReceiver == null ? LazyOptional.empty() : LazyOptional.of(() -> rsReceiver).cast();
        }
        if (capability == MjAPI.CAP_READABLE) {
//            return MjAPI.CAP_READABLE.cast(readable);
            return readable == null ? LazyOptional.empty() : LazyOptional.of(() -> readable).cast();
        }
        if (capability == MjAPI.CAP_PASSIVE_PROVIDER) {
//            return MjAPI.CAP_PASSIVE_PROVIDER.cast(provider);
            return provider == null ? LazyOptional.empty() : LazyOptional.of(() -> provider).cast();
        }
        if (capability == ForgeCapabilities.ENERGY) {
            return rfAutoConvert == null ? LazyOptional.empty() : LazyOptional.of(() -> rfAutoConvert).cast();
        }
        return LazyOptional.empty();
    }
}
