package net.morimori.mildomgiftutilty.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class StringUtils {
    public static String jsonBuilder(String inJson) {
        return inJson;
    }

    public static void txtWriter(String text, Path path) {
        try {
            FileWriter fw = new FileWriter(path.toString(), false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println(text);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
