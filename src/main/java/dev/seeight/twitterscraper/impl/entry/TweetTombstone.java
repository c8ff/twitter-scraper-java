package dev.seeight.twitterscraper.impl.entry;

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.impl.item.NotificationF;
import dev.seeight.twitterscraper.util.JsonHelper;

public class TweetTombstone extends Entry {
    public NotificationF.RichMessage text;

    public static TweetTombstone fromJson(JsonHelper h, JsonObject json) {
        TweetTombstone t = new TweetTombstone();
        t.text = NotificationF.RichMessage.fromJson(h, h.set(json).next("tombstone").object("text"));
        return t;
    }
}
