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
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.util.JsonHelper;

public class TimelineReplaceEntry extends Instruction {
	@SerializedName("entry_id_to_replace")
	public String entryIdToReplace;
	public Entry entry;

	public static TimelineReplaceEntry fromJson(Gson gson, JsonHelper h, JsonObject instruction) {
		h.set(instruction);

		TimelineReplaceEntry e = new TimelineReplaceEntry();
		e.type = h.string("type");

		if (!e.type.equals("TimelineReplaceEntry")) throw new IllegalStateException("Unsupported type.");

		e.entryIdToReplace = h.string("entry_id_to_replace");
		e.entry = Entry.parseEntry(gson, h, h.object("entry"));
		return e;
	}
}
