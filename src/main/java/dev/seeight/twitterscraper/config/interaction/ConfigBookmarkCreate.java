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

package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import java.net.URI;
import java.net.URISyntaxException;

public class ConfigBookmarkCreate implements IConfigJsonTree<Boolean> {
	public final String tweetId;

	public ConfigBookmarkCreate(String tweetId) {
		this.tweetId = tweetId;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("CreateBookmark").url;
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		return TwitterApi.newJsonPostRequest(uri, "{\"variables\":{\"tweet_id\":\"" + this.tweetId + "\"},\"queryId\":\"" + graphQL.get("CreateBookmark").queryId + "\"}");
	}

	@Override
	public Boolean fromJson(JsonElement element, Gson gson) {
		return new JsonHelper(element).query("data", "tweet_bookmark_put").getAsString().equals("Done");
	}
}
