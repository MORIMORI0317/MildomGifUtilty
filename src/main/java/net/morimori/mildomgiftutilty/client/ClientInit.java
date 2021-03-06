package net.morimori.mildomgiftutilty.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.morimori.mildomgiftutilty.client.handler.ClientHandler;
import net.morimori.mildomgiftutilty.util.MappingUtil;

import java.io.File;
import java.util.List;

public class ClientInit {
    public static void init() {

        String defaultResourcePacks = MappingUtil.isMapping() ? "defaultResourcePacks" : "field_110449_ao";

        List<IResourcePack> list = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), defaultResourcePacks);
        list.add(new FolderResourcePack(new File("mildomgiftutilty\\resource")));
        Minecraft.getMinecraft().refreshResources();

        MinecraftForge.EVENT_BUS.register(ClientHandler.class);
    }


}
