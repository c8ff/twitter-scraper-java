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

package dev.seeight.twitterscraper.impl.item;

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class Cursor extends Entry {
	public String value;
	public CursorType cursorType;
	public @Nullable DisplayTreatment displayTreatment;

	public static Cursor fromEntryContent(JsonObject content, JsonHelper h) {
		h.set(content);

		Cursor s = new Cursor();
		s.value = h.string("value");
		s.cursorType = CursorType.fromString(h.string("cursorType"));

		if (h.has("displayTreatment")) {
			h.next("displayTreatment");

			DisplayTreatment d = new DisplayTreatment();
			d.actionText = h.string("actionText");
			d.labelText = h.string("labelText", null);

			s.displayTreatment = d;
		}

		return s;
	}

	public static class DisplayTreatment {
		public String actionText;
		public String labelText;
	}

	public enum CursorType {
		BOTTOM,
		TOP,
		ShowMoreThreads,
		ShowMore,
		ShowMoreThreadsPrompt;

		public static CursorType fromString(String str) {
			for (CursorType value : values()) {
				if (value.name().equalsIgnoreCase(str)) {
					return value;
				}
			}

			throw new IllegalArgumentException("new cursor type dropped: " + str);
		}
	}
}
