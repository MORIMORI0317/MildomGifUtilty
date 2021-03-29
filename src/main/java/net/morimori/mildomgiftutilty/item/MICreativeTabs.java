package net.morimori.mildomgiftutilty.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.morimori.mildomgiftutilty.MildomGiftUtilty;

public class MICreativeTabs extends CreativeTabs {

    public static CreativeTabs MODTAB = new MICreativeTabs(MildomGiftUtilty.MODID);

    public MICreativeTabs(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.APPLE);
    }
}