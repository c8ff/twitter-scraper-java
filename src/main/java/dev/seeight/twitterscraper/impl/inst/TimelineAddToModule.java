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
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class TimelineAddToModule extends EntriesInstruction {
	public static TimelineAddToModule fromJson(Gson gson, JsonHelper h, JsonElement element) {
		TimelineAddToModule e = new TimelineAddToModule();

		e.type = h.set(element).string("type");
		if (!e.type.equals("TimelineAddToModule"))
			throw new IllegalStateException("Unsupported instruction type: " + e.type);

		List<Entry> entries = new ArrayList<>();
		JsonArray jModuleItems = h.set(element).array("moduleItems");
		for (JsonElement _item : jModuleItems) {
			if (!(_item instanceof JsonObject item)) continue;

			h.set(item);
			String entryId = h.string("entryId");
			JsonObject mainItem = h.object("item");
			Entry i = Entry.parseTimelineItem(entryId, "-1", gson, h, mainItem);
			if (i != null) entries.add(i);
		}

		e.entries = entries;
		return e;
	}
}
