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
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Note: this seems to always return empty tweets (and by extension, always returns true).
 */
public class ConfigDeleteTweet implements IConfigJsonTree<Boolean> {
	public final String tweetId;

	public ConfigDeleteTweet(String tweetId) {
		this.tweetId = tweetId;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		return api.getGraphQLOperation("DeleteTweet").getBaseUrl();
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
		var op = api.getGraphQLOperation("DeleteTweet");
		return TwitterApi.jsonPostReq(url, "{\"variables\":{\"tweet_id\":\"" + tweetId + "\",\"dark_request\":false},\"queryId\":\"" + op.getId() + "\"}");
	}

	@Override
	public Boolean fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return new JsonHelper(element).next("data").next("delete_tweet").next("tweet_results").object().isEmpty();
	}
}
