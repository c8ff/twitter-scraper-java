package dev.seeight.twitterscraper.impl.api2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

public class GlobalObjects {
    public Map<String, LegacyUser> users;
    public Map<String, LegacyTweet> tweets;

    public static GlobalObjects fromJson(JsonHelper h, JsonObject z) {
        h.set(z);
        var i = new GlobalObjects();
        i.users = new HashMap<>();
        i.tweets = new HashMap<>();
        for (Map.Entry<String, JsonElement> a : h.object("users").entrySet()) {
            i.users.put(a.getKey(), LegacyUser.fromJson(h, a.getValue().getAsJsonObject()));
        }
        for (Map.Entry<String, JsonElement> a : h.set(z).object("tweets").entrySet()) {
            i.tweets.put(a.getKey(), LegacyTweet.fromJson(h, a.getValue().getAsJsonObject()));
        }
        return i;
    }
}
