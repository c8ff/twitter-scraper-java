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
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.SendType;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.timeline.LatestTimeline;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigLatestTimeline implements IConfigJsonTree<LatestTimeline> {
	public int count = 20;
	public String cursor;
	public boolean includePromotedContent = true;
	public boolean latestControlAvailable = true;
	public String requestContext;
	public String[] seenTweetIds;

	public transient @NotNull SendType sendType = SendType.GET;

	public ConfigLatestTimeline(String cursor, String... seenTweetIds) {
		this.cursor = cursor;
		this.seenTweetIds = seenTweetIds;
		if (cursor == null) {
			requestContext = "launch";
		}
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
		// No method body for GET request.
		if (sendType == SendType.GET) {
			return IConfigJsonTree.super.createRequest(gson, url, api);
		}

		var a = api.getGraphQLOperation("HomeLatestTimeline");
		String variables = gson.toJson(this);
		String features = a.buildFeatures();
		String queryId = a.getId();
		return TwitterApi.jsonPostReq(url, "{\"variables\":" + variables + ",\"features\":" + features + ",\"queryId\":\"" + queryId + "\"}");
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("HomeLatestTimeline");
		if (sendType == SendType.POST) return op.getBaseUrl();
		return op.getUrl(gson.toJson(this));
	}

	@Override
	public LatestTimeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return LatestTimeline.fromJson(gson, element);
	}
}
