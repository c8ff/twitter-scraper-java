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

package dev.seeight.twitterscraper.impl.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

public class NotificationsTimeline extends Timeline {
	public String userId;
	public String timelineId;

	public static NotificationsTimeline fromJson(Gson gson, JsonElement element) {
		JsonHelper h = new JsonHelper(element);
		h.next("data").next("viewer_v2").next("user_results").next("result");

		NotificationsTimeline ts = GsonUtil.createObject(gson, NotificationsTimeline.class);

		ts.userId = h.string("rest_id");
		ts.timelineId = h.next("notification_timeline").string("id");
		ts.instructions = Instruction.fromInstructionsJson(gson, h, h.next("timeline").array("instructions"));
		return ts;
	}
}
