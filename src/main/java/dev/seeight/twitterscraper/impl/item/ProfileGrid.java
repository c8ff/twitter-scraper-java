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

package dev.seeight.twitterscraper.impl.item;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Replacement: {@link TimelineModule}
 */
@Deprecated
public class ProfileGrid extends Entry {
	public List<Entry> items;

	public static ProfileGrid fromEntryJson(Gson gson, JsonHelper h, JsonObject object) {
		JsonArray items = h.set(object).next("content").next("items").array();

		List<Entry> entries = new ArrayList<>();

		for (JsonElement _item : items) {
			if (!(_item instanceof JsonObject item)) continue;

			Entry e = Entry.parseEntry(gson, h, item);
			if (e != null) entries.add(e);
		}

		ProfileGrid e = new ProfileGrid();
		e.items = entries;
		return e;
	}
}
