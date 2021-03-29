package net.morimori.mildomgiftutilty.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class StringUtils {
    public static String jsonBuilder(String inJson) {
        return inJson;
    }

    public static void txtWriter(String text, Path path) {
        try {
            byte[] datas = text.getBytes(StandardCharsets.UTF_8);
            Files.write(path, datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
