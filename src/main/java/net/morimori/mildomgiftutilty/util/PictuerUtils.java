package net.morimori.mildomgiftutilty.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PictuerUtils {
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage outImage = new BufferedImage(width, height, image.getType());
        outImage.createGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
        return outImage;
    }
}
