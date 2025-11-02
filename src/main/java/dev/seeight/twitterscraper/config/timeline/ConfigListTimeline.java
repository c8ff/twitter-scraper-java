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

package dev.seeight.twitterscraper.config.timeline;

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
import java.util.List;

public class ConfigListTimeline implements IConfigJsonTree<Timeline> {
	public final @NotNull String listId;
	public final @Nullable String cursor;
	public int count;

	public ConfigListTimeline(@NotNull String listId, @Nullable String cursor) {
		this.listId = listId;
		this.cursor = cursor;
	}

	@Override
	public Timeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		Timeline t = new Timeline() {
		};

		JsonHelper h = new JsonHelper(element);
		// data.list.tweets_timeline.timeline.instructions
		JsonArray f = h.next("data").next("list").next("tweets_timeline").next("timeline").next("instructions").array();
		t.instructions = Instruction.fromInstructionsJson(gson, h, f);

		return t;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("ListLatestTweetsTimeline");
		return op.getUrl(gson.toJson(this));
	}
}
