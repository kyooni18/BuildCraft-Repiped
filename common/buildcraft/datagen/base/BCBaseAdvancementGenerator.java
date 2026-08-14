package buildcraft.datagen.base;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class BCBaseAdvancementGenerator implements DataProvider {
    protected static AdvancementHolder ROOT;
    protected static AdvancementHolder GUIDE;
    public static AdvancementHolder MARKERS;
    public static AdvancementHolder GEARS;
    public static AdvancementHolder WRENCHED;
    protected static final Criterion<ImpossibleTrigger.TriggerInstance> IMPOSSIBLE =
            CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance());

    private final PackOutput output;
    private final ExistingFileHelper fileHelperIn;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public BCBaseAdvancementGenerator(PackOutput output, ExistingFileHelper fileHelperIn, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.fileHelperIn = fileHelperIn;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return registries.thenCompose(provider -> {
            Path path = this.output.getOutputFolder();
            Set<ResourceLocation> set = Sets.newHashSet();
            List<CompletableFuture<?>> list = new ArrayList<>();
            Consumer<AdvancementHolder> consumer = advancement -> {
                if (!set.add(advancement.id())) {
                    throw new IllegalStateException("Duplicate advancement " + advancement.id());
                }
                list.add(DataProvider.saveStable(cache, provider, Advancement.CODEC, advancement.value(), createPath(path, advancement)));
            };
            registerAdvancements(consumer, fileHelperIn, provider);
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    protected abstract void registerAdvancements(Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper, HolderLookup.Provider registries);

    private static Path createPath(Path path, AdvancementHolder advancement) {
        ResourceLocation id = advancement.id();
        return path.resolve("data/" + id.getNamespace() + "/advancements/" + id.getPath() + ".json");
    }

    protected static ItemPredicate tag(TagKey<Item> tag) {
        return ItemPredicate.Builder.item().of(tag).build();
    }
}
