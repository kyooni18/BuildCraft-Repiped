package buildcraft.robotics.item;

import buildcraft.lib.item.IItemBuildCraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public class ItemRobotGoggles extends ArmorItem implements IItemBuildCraft {
    private final String idBC;

    public ItemRobotGoggles(String idBC, Properties properties) {
        // super(ArmorMaterial.CHAIN, 0, 0);
        super(ArmorMaterials.CHAIN, Type.HELMET, properties);
        // setCreativeTab(BCCreativeTab.get("main"));
        this.idBC = idBC;
        init();
    }

//    @Override
//    public ArmorProperties getProperties(LivingEntity player, ItemStack armor, DamageSource source, double damage, int slot) {
//        return new ArmorProperties(0, 0, 0);
//    }

//    @Override
//    public int getArmorDisplay(Player player, ItemStack armor, int slot) {
//        return 0;
//    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        // Never damaged
        return 0;
    }

//    @Override
//    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
//        return null;// TODO!
//    }

    @Override
    public String getIdBC() {
        return idBC;
    }

    // Calen
    private String unlocalizedName;

    @Override
    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return this.unlocalizedName;
    }
}
