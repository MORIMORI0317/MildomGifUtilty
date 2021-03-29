package net.morimori.mildomgiftutilty.mildom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.morimori.mildomgiftutilty.MildomGiftUtilty;
import net.morimori.mildomgiftutilty.util.PictuerUtils;
import net.morimori.mildomgiftutilty.util.StringUtils;
import net.morimori.mildomgiftutilty.util.URLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MildomGiftManager {
    private static final String GIFTDATA_URL = "https://raw.githubusercontent.com/MORIMORI0317/MildomGifUtilty/main/gifts.json";
    public static final Path DATA_PATH = Paths.get(MildomGiftUtilty.MODID);
    public static final Path RESOURCE_PATH = DATA_PATH.resolve("resource");
    public static final Path RESOURCE_TEXTUER_PATH = RESOURCE_PATH.resolve("assets").resolve(MildomGiftUtilty.MODID).resolve("textures").resolve("items");
    public static final Path RESOURCE_MODEL_PATH = RESOURCE_PATH.resolve("assets").resolve(MildomGiftUtilty.MODID).resolve("models").resolve("item");
    public static final Path RESOURCE_LANG_PATH = RESOURCE_PATH.resolve("assets").resolve(MildomGiftUtilty.MODID + "_lg").resolve("lang");
    private static final Logger LOGGER = LogManager.getLogger(MildomGiftManager.class);
    private static final Gson gson = new GsonBuilder().create();
    private static final Map<Integer, Gift> GIFTS = new HashMap<>();
    private static boolean init;

    public static boolean isClient() {
        return FMLCommonHandler.instance().getSide() == Side.CLIENT;
    }

    public static void init() {
        if (init)
            return;
        init = true;
        LOGGER.info("MildomGiftUtilty Initialized");

        try {
            fileInit();

            File mygiftdata = DATA_PATH.resolve("gifts.json").toFile();

            JsonObject data = URLUtils.getURLJsonResponse(GIFTDATA_URL);
            float dataver = data.get("data_version").getAsFloat();

            if (mygiftdata.exists()) {
                JsonObject myGiftDataJo = gson.fromJson(new InputStreamReader(new FileInputStream(mygiftdata), StandardCharsets.UTF_8), JsonObject.class);
                float myver = myGiftDataJo.get("data_version").getAsFloat();

                if (myver >= dataver) {
                    JsonArray giftsdata = myGiftDataJo.getAsJsonArray("gifts");
                    giftsdata.forEach(n -> {
                        JsonObject jo = n.getAsJsonObject();
                        GIFTS.put(jo.get("id").getAsInt(), new Gift(jo));
                    });
                } else {
                    updateGiftData(data);
                }
            } else {
                updateGiftData(data);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void fileInit() {
        DATA_PATH.toFile().mkdir();
        if (isClient()) {
            RESOURCE_TEXTUER_PATH.toFile().mkdirs();
            RESOURCE_MODEL_PATH.toFile().mkdirs();
            RESOURCE_LANG_PATH.toFile().mkdirs();
            try {
                File pcmt = DATA_PATH.resolve("resource").resolve("pack.mcmeta").toFile();

                if (!pcmt.exists()) {
                    String mt = "{\n" +
                            "    \"pack\": {\n" +
                            "        \"description\": \"mgu_ov resources\",\n" +
                            "        \"pack_format\": 3,\n" +
                            "        \"_comment\": \"\"\n" +
                            "    }\n" +
                            "}\n";
                    Files.write(pcmt.toPath(), mt.getBytes());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private static void updateGiftData(JsonObject data) {
        DATA_PATH.toFile().delete();
        fileInit();
        JsonArray giftsdata = data.getAsJsonArray("gifts");
        giftsdata.forEach(n -> {
            JsonObject jo = n.getAsJsonObject();
            GIFTS.put(jo.get("id").getAsInt(), new Gift(jo));
        });

        if (isClient()) {
            LOGGER.info("Mildom gift image dwonload start");

            Map<Integer, GiftLang> langs = new HashMap<>();

            giftsdata.forEach(n -> {
                JsonObject jo = n.getAsJsonObject();
                try {
                    LOGGER.info("Gift Dwonloding :" + jo.get("id") + " " + jo.get("id") + "/" + (giftsdata.size() - 1));
                    HttpURLConnection connection = (HttpURLConnection) new URL(jo.get("image").getAsString()).openConnection();
                    connection.addRequestProperty("User-Agent", URLUtils.USER_AGENT);
                    BufferedImage bi = ImageIO.read(connection.getInputStream());
                    int size = Math.max(bi.getWidth(), bi.getHeight());
                    BufferedImage outImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                    int ox = 0;
                    int oy = 0;
                    if (bi.getWidth() < bi.getHeight())
                        ox = (size - bi.getWidth()) / 2;
                    else if (bi.getHeight() < bi.getWidth())
                        oy = (size - bi.getHeight()) / 2;
                    outImage.createGraphics().drawImage(bi, ox, oy, null);

                    BufferedImage outImage2 = PictuerUtils.resize(outImage, mostTextuerSize(outImage.getWidth()), mostTextuerSize(outImage.getHeight()));
                    ImageIO.write(outImage2, "png", RESOURCE_TEXTUER_PATH.resolve(jo.get("id").getAsInt() + ".png").toFile());

                    String modelfl = "{\n" +
                            "    \"parent\": \"item/generated\",\n" +
                            "    \"textures\": {\n" +
                            "        \"layer0\": \"mildomgiftutilty:items/%s\"\n" +
                            "    }\n" +
                            "}\n";

                    Files.write(RESOURCE_MODEL_PATH.resolve(jo.get("id").getAsInt() + ".json"), String.format(modelfl, jo.get("id").getAsInt()).getBytes());

                    langs.put(jo.get("id").getAsInt(), new GiftLang(jo.get("name_en_us").getAsString(), jo.get("name_ja_jp").getAsString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            StringBuffer ensb = new StringBuffer();
            StringBuffer jpsb = new StringBuffer();

            langs.forEach((n, m) -> {
                String lol = "item.gift." + n + ".name";
                ensb.append(lol).append("=").append(m.getEn()).append("\n");
                jpsb.append(lol).append("=").append(m.getJa()).append("\n");
            });

            StringUtils.txtWriter(ensb.toString(), RESOURCE_LANG_PATH.resolve("en_us.lang"));
            StringUtils.txtWriter(jpsb.toString(), RESOURCE_LANG_PATH.resolve("ja_jp.lang"));

            LOGGER.info("Mildom gift image dwonload finish");
        }
        try {
            byte[] datas = StringUtils.jsonBuilder(gson.toJson(data)).getBytes(StandardCharsets.UTF_8);
            Files.write(DATA_PATH.resolve("gifts.json"), datas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int mostTextuerSize(int num) {
        return 128;
    }

    public static List<Gift> getGifts() {
        init();
        return new ArrayList<>(GIFTS.values());
    }

    public static Gift getGift(int id) {
        init();
        return GIFTS.get(id);
    }

    public static Gift getGiftByGiftID(int id) {
        init();
        return GIFTS.values().stream().filter(n -> n.getGiftId() == id).findFirst().orElse(GIFTS.get(0));
    }

    private static class GiftLang {
        private final String en;
        private final String ja;

        private GiftLang(String en, String ja) {
            this.en = en;
            this.ja = ja;
        }

        public String getEn() {
            return en;
        }

        public String getJa() {
            return ja;
        }
    }
}
