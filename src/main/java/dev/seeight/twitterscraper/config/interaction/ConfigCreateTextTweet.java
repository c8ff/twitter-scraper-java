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
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigCreateTextTweet implements IConfigJsonTree<Tweet> {
	@NotNull
	public final String text;
	@Nullable
	public final String inReplyToId;

	public ConfigCreateTextTweet(@NotNull String text, @Nullable String inReplyToId) {
		this.text = text;
		this.inReplyToId = inReplyToId;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("CreateTweet").url;
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		StringBuilder b = new StringBuilder();
		b.append("{\"variables\":{\"tweet_text\":\"");
		b.append(text);
		b.append("\",");

		if (inReplyToId != null) {
			b.append("\"reply\":{\"in_reply_to_tweet_id\":\"");
			b.append(inReplyToId);
			b.append("\",\"exclude_reply_user_ids\":[]},");
		}

		GraphQLMap.Entry e = graphQL.get("CreateTweet");
		String features = gson.toJson(e.features);
		String queryId = e.queryId;
		b.append("\"dark_request\":false,\"media\":{\"media_entities\":[],\"possibly_sensitive\":false},\"semantic_annotation_ids\":[]},\"features\":").append(features).append(",\"queryId\":\"").append(queryId).append("\"}");
		return TwitterApi.newJsonPostRequest(uri, b.toString());
	}

	@Override
	public Tweet fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		JsonHelper h = new JsonHelper(element).next("data").next("create_tweet").next("tweet_results").next("result");
		return Tweet.fromJson(gson, h.object(), h);
	}
}
