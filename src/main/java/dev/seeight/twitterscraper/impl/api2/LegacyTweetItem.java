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
