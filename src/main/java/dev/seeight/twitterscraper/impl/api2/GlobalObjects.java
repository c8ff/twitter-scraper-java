/*
 * twitter-scraper-java.main
 * Copyright (C) 2025 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
