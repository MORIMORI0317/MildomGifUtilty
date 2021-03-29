package net.morimori.mildomgiftutilty.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.morimori.mildomgiftutilty.MGUConfig;
import net.morimori.mildomgiftutilty.item.MGUItems;
import net.morimori.mildomgiftutilty.mildom.MildomGiftManager;
import net.morimori.mildomgiftutilty.util.PlayerUtil;
import net.morimori.mildomgiftutilty.util.ServerUtility;
import net.pmtf.mildomintegration.event.MildomEvent;

import java.util.List;

public class ServerHandler {
    @SubscribeEvent
    public static void onMildomGift(MildomEvent.MildomGiftEvent e) {
        try {
            if (ServerUtility.getServer() != null) {
                List<EntityPlayerMP> players = ServerUtility.getPlayers();
                for (EntityPlayerMP player : players) {
                    if (MGUConfig.GiftPlayerUUID.isEmpty() || player.getGameProfile().getId().toString().equals(MGUConfig.GiftPlayerUUID)) {
                        ItemStack stack = new ItemStack(MGUItems.GIFT, 1, MildomGiftManager.getGiftByGiftID(e.getGift()).getId());
                        PlayerUtil.giveItem(player, stack, e.getCont());
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
