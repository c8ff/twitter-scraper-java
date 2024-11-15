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
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

public class TimelinePinEntry extends Instruction {
	public Entry entry;

	public static TimelinePinEntry fromJson(Gson gson, JsonHelper h, JsonElement element) {
		TimelinePinEntry a1 = GsonUtil.createObject(gson, TimelinePinEntry.class);

		a1.type = h.set(element).string("type");

		if (!a1.type.equals("TimelinePinEntry")) {
			throw new IllegalArgumentException("Instruction type is not 'TimelineAddEntries'.");
		}

		a1.entry = Entry.parseEntry(gson, h, h.object("entry"));
		return a1;
	}
}
