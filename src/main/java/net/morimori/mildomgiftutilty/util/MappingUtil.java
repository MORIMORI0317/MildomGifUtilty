package net.morimori.mildomgiftutilty.util;

import net.minecraft.launchwrapper.Launch;

public class MappingUtil {
    public static boolean isMapping() {
        final Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        return deobfuscated != null && deobfuscated;
    }
}
