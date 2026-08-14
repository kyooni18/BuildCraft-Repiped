package buildcraft.datagen.base;

import buildcraft.api.BCModules;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BCBaseLootTableProvider extends LootTableProvider {
    @SuppressWarnings("unused")
    private final BCModules module;

    public BCBaseLootTableProvider(BCModules module, PackOutput output, Set<ResourceKey<LootTable>> requiredTables,
                                   List<SubProviderEntry> entries, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, requiredTables, entries, registries);
        this.module = module;
    }
}
