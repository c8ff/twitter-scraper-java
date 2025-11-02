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
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.timeline.SearchByRawQuery;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ConfigSearchTimeline implements IConfigJsonTree<SearchByRawQuery> {
	public final String rawQuery;
	public int count = 20;
	public final String cursor;
	public String querySource = QuerySource.typed_query.name();
	public ProductType product = ProductType.Top;

	public ConfigSearchTimeline(String rawQuery, String cursor) {
		this.rawQuery = rawQuery;
		this.cursor = cursor;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("SearchTimeline");
		return op.getUrl(gson.toJson(this));
	}

	@Override
	public SearchByRawQuery fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return SearchByRawQuery.fromJson(gson, new JsonHelper(element), element);
	}

    @Override
    public SearchByRawQuery resolve(OkHttpClient client, Request request, Gson gson) throws IOException, TwitterException {
        try (var response = client.newCall(request).execute()) {
            String responseStr = response.body().string();
            List<TwitterError> errors = new ArrayList<>();
            JsonElement json;
            try {
                json = JsonParser.parseString(responseStr);
            } catch (Throwable e) {
                throw new RuntimeException("Cannot parse JSON. Original response: " + responseStr, e);
            }
            SearchByRawQuery w = this.fromJson(IConfigJsonTree.assertErrors(json, request, errors), gson, errors);
            w.rateLimit = Integer.parseInt(response.header("x-rate-limit-limit"));
            w.rateLimitRemaining = Integer.parseInt(response.header("x-rate-limit-remaining"));
            w.rateLimitReset = Long.parseLong(response.header("x-rate-limit-reset"));
            return w;
        }
    }

    public enum QuerySource {
		typed_query,
		/**
		 * This is used on the 'quotes' tab a tweet interactions UI.
		 */
		tdqt
	}

	public enum ProductType {
		Top,
		Latest,
		People,
		Media,
		Lists
	}
}
