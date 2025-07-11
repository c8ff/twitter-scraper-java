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
