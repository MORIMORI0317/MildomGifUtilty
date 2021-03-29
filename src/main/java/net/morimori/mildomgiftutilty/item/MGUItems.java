package net.morimori.mildomgiftutilty.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.morimori.mildomgiftutilty.MildomGiftUtilty;

@Mod.EventBusSubscriber(modid = MildomGiftUtilty.MODID)
public class MGUItems {
    public static final Item GIFT = create("gift", new ItemGift());

    private static Item create(String name, Item item) {
        Item itm = item.setRegistryName(MildomGiftUtilty.MODID, name).setUnlocalizedName(name).setCreativeTab(MICreativeTabs.MODTAB);
        return itm;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(GIFT);
    }
}
