package net.morimori.mildomgiftutilty;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.morimori.mildomgiftutilty.client.ClientInit;
import net.morimori.mildomgiftutilty.handler.ServerHandler;
import net.morimori.mildomgiftutilty.mildom.MildomGiftManager;

@Mod(modid = MildomGiftUtilty.MODID, name = MildomGiftUtilty.NAME, version = MildomGiftUtilty.VERSION, dependencies = "required-after:mildomintegration@[1.2,)")
public class MildomGiftUtilty {
    public static final String MODID = "mildomgiftutilty";
    public static final String NAME = "Mildom Gift Utilty";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MildomGiftManager.init();
        MGUConfig.loadc(e);
        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
        if (e.getSide() == Side.CLIENT) {
            ClientInit.init();
        }
    }
}
