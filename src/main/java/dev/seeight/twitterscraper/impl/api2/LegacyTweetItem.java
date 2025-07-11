package dev.seeight.twitterscraper.impl.api2;

import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.util.JsonHelper;

public class LegacyTweetItem extends LegacyItem {
    public String id;
    public String displayType;
    public String displaySize;

    public LegacyTweetItem() {
    }

    public LegacyTweetItem(String id, String displayType, String displaySize) {
        this.id = id;
        this.displayType = displayType;
        this.displaySize = displaySize;
    }

    public static LegacyTweetItem fromJson(JsonHelper h, JsonElement z) {
        h.set(z);
        return new LegacyTweetItem(h.string("id"), h.string("displayType"), h.string("displaySize"));
    }
}
