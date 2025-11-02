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
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class ConfigFollowing implements IConfigJsonTree<ConfigFollowing.FollowingTimeline> {
	public final @NotNull String userId;
	public final @Nullable String cursor;
	public int count = 20;
	public boolean includePromotedContent = false;

	public ConfigFollowing(String userId) {
		this(userId, null);
	}

	public ConfigFollowing(@NotNull String userId, @Nullable String cursor) {
		this.userId = userId;
		this.cursor = cursor;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		return api.getGraphQLOperation("Following").getUrl(gson.toJson(this));
	}

	@Override
	public ConfigFollowing.FollowingTimeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		var m = new FollowingTimeline();

		JsonHelper h = new JsonHelper(element);
		h.next("data")
			.next("user")
			.next("result")
			.next("timeline");

		if (!h.has("timeline")) {
			m.instructions = Collections.emptyList();
			return m;
		}
		h.next("timeline");

		JsonArray instructions = h.array("instructions");
		m.instructions = Instruction.fromInstructionsJson(gson, h, instructions);
		return m;
	}

	public static class FollowingTimeline extends Timeline {
	}
}
