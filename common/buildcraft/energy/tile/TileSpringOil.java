package buildcraft.energy.tile;

import buildcraft.api.tiles.IDebuggable;
import buildcraft.core.tile.ITileOilSpring;
import buildcraft.energy.BCEnergyBlocks;
import buildcraft.lib.misc.AdvancementUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// We don't extend TileBC here because we have no need of any of its functions.
public class TileSpringOil extends BlockEntity implements IDebuggable, ITileOilSpring {

    private static final ResourceLocation ADVANCEMENT_PUMP_LARGE_OIL_WELL = ResourceLocation.parse(
            "buildcraftfactory:black_gold"
    );

    private final Map<GameProfile, PlayerPumpInfo> pumpProgress = new ConcurrentHashMap<>();

    /** An approximation of the total number of oil source blocks in the oil spring. The actual number will be less than
     * this, so this is taken as an approximation.
     * <p>
     * Note that this SHOULD NEVER be set! (Except by the generator, and readFromNbt) */
    public int totalSources;

    public TileSpringOil(BlockPos pos, BlockState blockState) {
        super(BCEnergyBlocks.springTile.get(), pos, blockState);
    }

    @Override
    public void onPumpOil(GameProfile profile, BlockPos oilPos) {
        if (profile == null) {
            // BCLog.logger.warn("Unknown owner for pump at " + pump.getPos());
            return;
        }
        PlayerPumpInfo info = pumpProgress.computeIfAbsent(profile, PlayerPumpInfo::new);
        info.lastPumpTick = level.getGameTime();
        info.sourcesPumped++;

        // BCLog.logger.info("Pumped " + info.sourcesPumped + " / " + totalSources + " at " + oilPos + " (for " +
        // System.identityHashCode(this) + ", "+getPos()+")");
        if (info.sourcesPumped >= totalSources * 7 / 8) {
            // BCLog.logger.info("Pumped nearly all oil blocks!");
            if (oilPos.equals(getBlockPos().above())) {
                AdvancementUtil.unlockAdvancement(profile.getId(), ADVANCEMENT_PUMP_LARGE_OIL_WELL);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        ListTag list = nbt.getList("pumpProgress", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            PlayerPumpInfo info = new PlayerPumpInfo(list.getCompound(i));
            pumpProgress.put(info.profile, info);
        }
    }

    @Override
//    public CompoundTag writeToNBT(CompoundTag nbt) {
    protected void saveAdditional(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("totalSources", totalSources);
        ListTag list = new ListTag();
        for (PlayerPumpInfo info : pumpProgress.values()) {
            list.add(info.writeToNbt());
        }
        nbt.put("pumpProgress", list);
    }

    @Override
//    public void getDebugInfo(List<String> left, List<String> right, Direction side)
    public void getDebugInfo(List<Component> left, List<Component> right, Direction side) {
//        left.add("totalSources = " + totalSources);
        left.add(Component.literal("totalSources = " + totalSources));
        boolean added = false;
        for (PlayerPumpInfo info : pumpProgress.values()) {
            if (!added) {
//                left.add("Player Progress:");
                left.add(Component.literal("Player Progress:"));
                added = true;
            }
//            left.add("  " + info.profile.getName() + " = " + info.sourcesPumped + " ( " + (level.getGameTime() - info.lastPumpTick) / 20 + "s )");
            left.add(Component.literal("  " + info.profile.getName() + " = " + info.sourcesPumped + " ( " + (level.getGameTime() - info.lastPumpTick) / 20 + "s )"));
        }
    }

    static class PlayerPumpInfo {
        final GameProfile profile;
        long lastPumpTick = -1;
        int sourcesPumped = 0;

        public PlayerPumpInfo(GameProfile profile) {
            this.profile = profile;
        }

        public PlayerPumpInfo(CompoundTag nbt) {
            CompoundTag profileTag = nbt.getCompound("profile");
            UUID id = profileTag.hasUUID("id") ? profileTag.getUUID("id") : Util.NIL_UUID;
            profile = new GameProfile(id, profileTag.getString("name"));
            lastPumpTick = nbt.getLong("lastPumpTick");
            sourcesPumped = nbt.getInt("sourcesPumped");
        }

        public CompoundTag writeToNbt() {
            CompoundTag nbt = new CompoundTag();
            CompoundTag profileTag = new CompoundTag();
            if (profile.getId() != null) {
                profileTag.putUUID("id", profile.getId());
            }
            if (profile.getName() != null) {
                profileTag.putString("name", profile.getName());
            }
            nbt.put("profile", profileTag);
            nbt.putLong("lastPumpTick", lastPumpTick);
            nbt.putInt("sourcesPumped", sourcesPumped);
            return nbt;
        }
    }
}
