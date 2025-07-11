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

package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigDeleteRetweet implements IConfigJsonTree<Boolean> {
	public final String tweetId;

	public ConfigDeleteRetweet(String tweetId) {
		this.tweetId = tweetId;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("DeleteRetweet").url;
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		return TwitterApi.newJsonPostRequest(uri, "{\"variables\":{\"source_tweet_id\":\"" + tweetId + "\",\"dark_request\":false},\"queryId\":\"" + graphQL.get("DeleteRetweet").queryId + "\"}");
	}

	@Override
	public Boolean fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		JsonHelper h = new JsonHelper(element);
		return h.next("data").next("unretweet").next("source_tweet_results").next("result").next("rest_id").string().equals(this.tweetId);
	}
}
