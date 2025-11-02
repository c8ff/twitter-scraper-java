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

package dev.seeight.twitterscraper.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

public class ConfigListsAdminTimeline implements IConfigJsonTree<Timeline> {
	public int variables = 100;

	@Override
	public Timeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return ListsAdminTimeline.fromJson(gson, element);
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		return api.getGraphQLOperation("ListsManagementPageTimeline").getUrl(gson.toJson(this));
	}

	public static class ListsAdminTimeline extends Timeline {
		public static ListsAdminTimeline fromJson(Gson gson, JsonElement element) {
			JsonHelper h = new JsonHelper(element);

			h.next("data").next("viewer");

			var m = new ListsAdminTimeline();
			if (!h.has("list_management_timeline")) {
				m.instructions = Collections.emptyList();
				return m;
			}

			h.next("list_management_timeline");

			if (!h.has("timeline")) {
				m.instructions = Collections.emptyList();
				return m;
			}

			JsonArray instructions = h.query("timeline", "instructions").getAsJsonArray();
			m.instructions = Instruction.fromInstructionsJson(gson, h, instructions);
			return m;
		}
	}
}
