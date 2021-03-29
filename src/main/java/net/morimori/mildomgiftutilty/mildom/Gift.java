package net.morimori.mildomgiftutilty.mildom;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Gift {
    private final int id;
    private final int giftId;
    private final long emojiId;
    private final int price;
    private final String image;
    private final Map<String, String> names = new HashMap<>();

    public Gift(JsonObject jo) {
        this.id = jo.get("id").getAsInt();
        this.giftId = jo.get("gift_id").getAsInt();
        this.emojiId = jo.get("emoji_id").getAsLong();
        this.price = jo.get("price").getAsInt();
        this.image = jo.get("image").getAsString();
        jo.entrySet().stream().filter(n -> n.getKey().contains("name_")).forEach(n -> {
            names.put(n.getKey().replace("name_", ""), n.getValue().getAsString());
        });
    }

    public int getId() {
        return id;
    }

    public int getGiftId() {
        return giftId;
    }

    public int getPrice() {
        return price;
    }

    public long getEmojiId() {
        return emojiId;
    }

    public String getImage() {
        return image;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public String getName(String lang) {

        if (!names.containsKey(lang))
            return names.getOrDefault("en_us", null);

        return names.get(lang);
    }

    public String getName() {
        return names.getOrDefault("ja_jp", null);
    }
}
