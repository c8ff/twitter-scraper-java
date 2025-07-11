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
