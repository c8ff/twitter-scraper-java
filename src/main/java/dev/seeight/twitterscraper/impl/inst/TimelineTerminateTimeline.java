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

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.item.Cursor;
import dev.seeight.twitterscraper.util.JsonHelper;

public class TimelineTerminateTimeline extends Instruction {
	public Cursor.CursorType direction;

	public static TimelineTerminateTimeline fromJson(JsonHelper h, JsonObject object) {
		h.set(object);

		TimelineTerminateTimeline e = new TimelineTerminateTimeline();
		e.type = h.string("type");

		if (!e.type.equals("TimelineTerminateTimeline"))
			throw new IllegalStateException("Unsupported instruction type: " + e.type);

		e.direction = Cursor.CursorType.fromString(h.string("direction"));

		return e;
	}
}
