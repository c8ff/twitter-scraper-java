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

package dev.seeight.twitterscraper.impl.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

public class LatestTimeline extends Timeline {
	public static LatestTimeline fromJson(Gson gson, JsonElement element) {
		JsonHelper h = new JsonHelper(element);
		JsonArray instructions = h.next("data").next("home").next("home_timeline_urt").next("instructions").array();

		LatestTimeline ts = GsonUtil.createObject(gson, LatestTimeline.class);
		ts.instructions = Instruction.fromInstructionsJson(gson, h, instructions);
		return ts;
	}
}
