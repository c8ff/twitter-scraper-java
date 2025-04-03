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

package dev.seeight.twitterscraper.impl.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.Collections;

public class UserTweetsReplies extends Timeline {
	public static UserTweetsReplies fromJson(Gson gson, JsonElement json) {
		JsonHelper h = new JsonHelper(json);

		UserTweetsReplies t = GsonUtil.createObject(gson, UserTweetsReplies.class);

		h.next("data").next("user");
		if (!h.has("result")) {
			t.instructions = Collections.emptyList();
			return t;
		}

		JsonArray instructions = h.query("result", "timeline", "timeline", "instructions").getAsJsonArray();
		t.instructions = Instruction.fromInstructionsJson(gson, h, instructions);
		return t;
	}
}
