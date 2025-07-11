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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.upload.PartiallyUploadedMedia;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ConfigCreateTextTweet implements IConfigJsonTree<Tweet> {
	@NotNull
	public final String text;
	@Nullable
	public final String inReplyToId;
	@Nullable
	public Media media;

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
		JsonObject variables = new JsonObject();
		variables.addProperty("tweet_text", this.text);
		variables.addProperty("dark_request", false);
		variables.addProperty("disallowed_reply_options", (String) null);

		if (this.inReplyToId != null) {
			JsonObject reply = new JsonObject();
			reply.addProperty("in_reply_to_tweet_id", this.inReplyToId);
			reply.add("exclude_reply_user_ids", new JsonArray());

			variables.add("reply", reply);
		}

		if (this.media != null) {
			variables.add("media", gson.toJsonTree(this.media));
		} else {
			JsonObject media = new JsonObject();
			media.addProperty("possibly_sensitive", false);
			media.add("media_entities", new JsonArray());
			variables.add("media", media);
		}

		StringBuilder b = new StringBuilder();

		b.append("{\"variables\":").append(variables).append(",");

		GraphQLMap.Entry e = graphQL.get("CreateTweet");
		String features = gson.toJson(e.features);
		String queryId = e.queryId;
		b.append("\"features\":").append(features).append(",\"queryId\":\"").append(queryId).append("\"}");
		return TwitterApi.newJsonPostRequest(uri, b.toString());
	}

	@Override
	public Tweet fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		JsonHelper h = new JsonHelper(element).next("data").next("create_tweet").next("tweet_results").next("result");
		return Tweet.fromJson(gson, h.object(), h);
	}

	public static class Media {
		public Entity[] media_entities;
		public boolean possibly_sensitive;

		public static class Entity {
			public String media_id;
			public String[] tagged_users;

			public Entity() {
			}

			public Entity(String media_id, String[] tagged_users) {
				this.media_id = media_id;
				this.tagged_users = tagged_users;
			}
		}

		public static Media ofUploadedMedia(PartiallyUploadedMedia... p) {
			List<Entity> e = new ArrayList<>();
			for (PartiallyUploadedMedia m : p) {
				e.add(new Entity(m.media_id_string, new String[0]));
			}
			Media m = new Media();
			m.media_entities = e.toArray(new Entity[0]);
			return m;
		}

		public static Media singleMedia(String mediaId) {
			Media m = new Media();
			m.media_entities = new Entity[]{
				new Entity(mediaId, new String[0])
			};
			m.possibly_sensitive = false;
			return m;
		}
	}
}
