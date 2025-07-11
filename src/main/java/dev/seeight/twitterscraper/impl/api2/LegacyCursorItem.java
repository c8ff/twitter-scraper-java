package dev.seeight.twitterscraper.impl.api2;

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.item.Cursor;
import dev.seeight.twitterscraper.util.JsonHelper;

public class LegacyCursorItem extends LegacyItem {
    public String value;
    public Cursor.CursorType type;

    public LegacyCursorItem() {
    }

    public LegacyCursorItem(String value, Cursor.CursorType type) {
        this.value = value;
        this.type = type;
    }

    public static LegacyItem fromJson(JsonHelper h, JsonObject z) {
        h.set(z);
        return new LegacyCursorItem(
                h.string("value"),
                Cursor.CursorType.fromString(h.string("cursorType"))
        );
    }
}
