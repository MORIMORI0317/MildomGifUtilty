package net.morimori.mildomgiftutilty.compat;

import net.minecraftforge.fml.common.Loader;

public class ChanceCubeCompat {
    public static boolean isLoaded() {
        return Loader.isModLoaded("chancecubes");
    }
}
