package net.morimori.mildomgiftutilty.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.morimori.mildomgiftutilty.MildomGiftUtilty;
import net.morimori.mildomgiftutilty.item.MGUItems;
import net.morimori.mildomgiftutilty.mildom.Gift;
import net.morimori.mildomgiftutilty.mildom.MildomGiftManager;
import net.pmtf.mildomintegration.compat.EmojicordCompat;
import net.pmtf.mildomintegration.event.MildomEvent;
import net.pmtf.mildomintegration.mildom.Mildom;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e) {
        MildomGiftManager.getGifts().forEach(n -> {
            ModelLoader.setCustomModelResourceLocation(MGUItems.GIFT, n.getId(), new ModelResourceLocation(new ResourceLocation(MildomGiftUtilty.MODID, String.valueOf(n.getId())), "inventory"));
        });
    }

    @SubscribeEvent
    public static void onMildomGift(MildomEvent.MildomGiftEvent e) {
        if (mc.player != null && Mildom.isShowGift()) {
            Gift gift = MildomGiftManager.getGiftByGiftID(e.getGift());
            String giftName = EmojicordCompat.isLoaded() ? EmojicordCompat.getEmojiString(gift.getEmojiId()) : I18n.format("item.gift." + gift.getId() + ".name");
            ITextComponent comp = new TextComponentTranslation("message.migift", e.getMildomUserNameAndLevel(), giftName + (e.getCont() > 1 ? ("×" + e.getCont()) : ""));
            mc.player.sendStatusMessage(comp, false);
        }
    }
}
