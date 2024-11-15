/*
 * twitter-scraper-java
 * Copyright (C) 2024 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.impl.inst;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class TimelineAddEntries extends Instruction {
	public List<Entry> entries;

	public static TimelineAddEntries fromJson(Gson gson, JsonHelper h, JsonElement element) {
		h.set(element);

		TimelineAddEntries e = GsonUtil.createObject(gson, TimelineAddEntries.class);
		e.type = h.string("type");
		if (!e.type.equals("TimelineAddEntries")) throw new IllegalArgumentException("Unsupported instruction type.");

		List<Entry> entries = new ArrayList<>();
		JsonArray jEntries = h.array("entries").getAsJsonArray();

		for (JsonElement _entry : jEntries) {
			if (!(_entry instanceof JsonObject entry)) continue;

			Entry i = Entry.parseEntry(gson, h, entry);
			if (i != null) entries.add(i);
		}

		e.entries = entries;
		return e;
	}
}
