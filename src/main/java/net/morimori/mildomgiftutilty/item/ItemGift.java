package net.morimori.mildomgiftutilty.item;

import chanceCubes.registry.global.GlobalCCRewardRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.morimori.mildomgiftutilty.compat.ChanceCubeCompat;
import net.morimori.mildomgiftutilty.mildom.Gift;
import net.morimori.mildomgiftutilty.mildom.MildomGiftManager;
import net.pmtf.mildomintegration.compat.EmojicordCompat;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemGift extends ItemFood {
    public ItemGift() {
        super(4, 1.2F, false);
        this.setHasSubtypes(true);
        this.setAlwaysEdible();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            MildomGiftManager.getGifts().stream().filter(n -> n.getId() != 0).forEach(n -> items.add(new ItemStack(this, 1, n.getId())));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (EmojicordCompat.isLoaded())
            tooltip.add(EmojicordCompat.getPriceEmoji() + " " + MildomGiftManager.getGift(stack.getItemDamage()).getPrice());
        else
            tooltip.add(I18n.format("item.gift.price", MildomGiftManager.getGift(stack.getItemDamage()).getPrice()));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + "." + stack.getItemDamage() + ".name").trim();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            Gift gift = MildomGiftManager.getGift(stack.getItemDamage());
            int xp = gift.getPrice() * gift.getPrice() - 4 * gift.getPrice();
            player.addExperience(xp);

            Random r = new Random(gift.getGiftId());
            List<Potion> poss = ForgeRegistries.POTIONS.getValues();
            player.addPotionEffect(new PotionEffect(poss.get(r.nextInt(poss.size())), (int) (20 * 60 * 3 * (r.nextFloat() + 1f)), r.nextInt(2)));

            if (ChanceCubeCompat.isLoaded()) {

                int chancePrice = Math.max(gift.getPrice(), 3);

                double chance = (double) chancePrice / 100d;
                double giantChance = (double) chancePrice / 3000d;

                BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);

                int cont = (int) Math.floor(chance);
                double rcchance = chance - cont;
                for (int i = 0; i < cont; i++) {
                    GlobalCCRewardRegistry ccr = GlobalCCRewardRegistry.DEFAULT;
                    if (player.getRNG().nextDouble() <= giantChance) {
                        ccr = GlobalCCRewardRegistry.GIANT;
                    }
                    ccr.triggerRandomReward(worldIn, pos, player, r.nextInt(200) - 100);
                }

                if (player.getRNG().nextDouble() <= rcchance) {
                    GlobalCCRewardRegistry ccr = GlobalCCRewardRegistry.DEFAULT;
                    if (player.getRNG().nextDouble() <= giantChance) {
                        ccr = GlobalCCRewardRegistry.GIANT;
                    }
                    ccr.triggerRandomReward(worldIn, pos, player, r.nextInt(200) - 100);
                }
            }
        }
    }
}
