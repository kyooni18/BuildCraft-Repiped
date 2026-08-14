package buildcraft.test.api.transport.pipe;

import buildcraft.api.transport.pipe.PipeDefinition;
import buildcraft.test.VanillaSetupBaseTester;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/** Regression coverage for Minecraft 1.21 item component validation. */
public class PipeDefinitionPropertiesTester extends VanillaSetupBaseTester {
    @Test
    public void defaultPipeItemPropertiesAreValidAndStackable() throws Exception {
        PipeDefinition.PipeDefinitionBuilder builder = new PipeDefinition.PipeDefinitionBuilder();

        Method validate = Item.Properties.class.getDeclaredMethod("buildAndValidateComponents");
        validate.setAccessible(true);
        DataComponentMap components = (DataComponentMap) validate.invoke(builder.properties);

        Assert.assertEquals(64, (int) components.getOrDefault(DataComponents.MAX_STACK_SIZE, 1));
        Assert.assertFalse(components.has(DataComponents.DAMAGE));
    }
}
