package net.morimori.mildomgiftutilty.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.stream.IntStream;

public class PlayerUtil {
    public static void giveItem(EntityPlayer player, ItemStack stack, int cont) {
        if (cont <= 64) {
            stack.setCount(cont);
            giveItem(player, stack);
            return;
        }

        int ct = cont / 64;

        IntStream.range(0, ct).forEach(n -> {
            ItemStack cop = stack.copy();
            cop.setCount(64);
            giveItem(player, cop);
        });
        int am = cont - 64 * ct;
        stack.setCount(am);
        giveItem(player, stack);
    }

    public static void giveItem(EntityPlayer player, ItemStack stack) {
        if (!player.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        }
    }
}
