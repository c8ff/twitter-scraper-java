package dev.seeight.twitterscraper.impl.api2.instruction;

import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.impl.api2.LegacyCursorItem;
import dev.seeight.twitterscraper.impl.api2.LegacyItem;
import dev.seeight.twitterscraper.impl.api2.LegacyTweetItem;
import dev.seeight.twitterscraper.util.JsonHelper;

public class LegacyEntry {
    public String entryId;
    public String sortIndex;
    public LegacyItem item;



    public static LegacyEntry fromJson(JsonHelper h, JsonElement z) {
        h.set(z);
        var i = new LegacyEntry();
        i.entryId = h.string("entryId");
        i.sortIndex = h.string("sortIndex");
        h.next("content");
        if (!h.has("item")) return i;
        h.next("item").next("content");
        if (h.has("tweet")) {
            i.item = LegacyTweetItem.fromJson(h, h.object("tweet"));
        } else if (h.has("cursor")) {
            i.item = LegacyCursorItem.fromJson(h, h.object("cursor"));
        } else {
            throw new IllegalStateException("Unknown entry type: " + h.object().toString());
        }
        return i;
    }
}
