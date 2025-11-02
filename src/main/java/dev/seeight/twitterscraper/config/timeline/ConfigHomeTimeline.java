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
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.SendType;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigHomeTimeline implements IConfigJsonTree<Timeline> {
	public int count = 20;
	public final String cursor;
	public boolean includePromotedContent = true;
	public boolean latestControlAvailable = true;
	public String requestContext;
	public boolean withCommunity = true;

	public transient @NotNull SendType sendType = SendType.GET;

	public ConfigHomeTimeline(String cursor) {
		this.cursor = cursor;
		if (cursor == null) {
			requestContext = "launch";
		}
	}

	@Override
	public Timeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		Timeline tl = new Timeline() {
		};

		JsonHelper h = new JsonHelper(element);
		h.next("data").next("home").next("home_timeline_urt").next("instructions");
		tl.instructions = Instruction.fromInstructionsJson(gson, h, h.array());

		return tl;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("HomeTimeline");
		if (sendType == SendType.POST) return op.getBaseUrl();
        return op.getUrl(gson.toJson(this));
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
		// No method body for GET request.
		if (sendType == SendType.GET) {
			return IConfigJsonTree.super.createRequest(gson, url, api);
		}

		var op = api.getGraphQLOperation("HomeTimeline");
		String variables = gson.toJson(this);
		String features = op.buildFeatures();
		String queryId = op.getId();
		return TwitterApi.jsonPostReq(url, "{\"variables\":" + variables + ",\"features\":" + features + ",\"queryId\":\"" + queryId + "\"}");
	}
}
