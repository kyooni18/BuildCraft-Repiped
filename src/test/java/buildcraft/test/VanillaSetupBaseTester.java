package buildcraft.test;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import org.junit.BeforeClass;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class VanillaSetupBaseTester {
    @BeforeClass
    public static void init() {
        System.out.println("INIT");
        PrintStream sysOut = System.out;
        InputStream sysIn = System.in;

        setupEmptyForgeModList();
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        System.setIn(sysIn);
        System.setOut(sysOut);
    }

    /**
     * Vanilla bootstrap now passes item component construction through Forge hooks. In a normal
     * game launch FMLLoader initializes this before Minecraft registries are bootstrapped, but
     * plain JUnit does not run ModLauncher. Supply the empty loading state that these vanilla-only
     * tests need so Forge hooks can initialize without pretending to load any mods.
     */
    private static void setupEmptyForgeModList() {
        try {
            LoadingModList loadingModList = LoadingModList.of(List.of(), List.of(), List.of(), List.of(), Map.of());

            Field loadingModListField = FMLLoader.class.getDeclaredField("loadingModList");
            loadingModListField.setAccessible(true);
            loadingModListField.set(null, loadingModList);

            Field gameLayerField = FMLLoader.class.getDeclaredField("gameLayer");
            gameLayerField.setAccessible(true);
            gameLayerField.set(null, ModuleLayer.boot());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to initialize Forge's empty test loading state", e);
        }
    }
}
